package com.littlegig.app.services

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.contactsDataStore by preferencesDataStore(name = "contacts_cache")

@Singleton
class ContactsService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneNumberService: PhoneNumberService
) {
    private val KEY_HASH = stringPreferencesKey("contacts_hash")
    private val KEY_CACHED = stringPreferencesKey("contacts_cached")
    private val KEY_UPDATED_AT = longPreferencesKey("contacts_updated_at")
    private val CACHE_TTL_MS = 6 * 60 * 60 * 1000L // 6 hours

    fun getNormalizedPhoneContacts(defaultRegion: String = "KE"): List<String> {
        val resolver: ContentResolver = context.contentResolver

        // Attempt cached return
        val now = System.currentTimeMillis()
        val (cachedList, cachedHash, cachedAt) = runBlocking {
            val prefs = context.contactsDataStore.data.first()
            Triple(
                prefs[KEY_CACHED]?.split(',')?.filter { it.isNotBlank() } ?: emptyList(),
                prefs[KEY_HASH],
                prefs[KEY_UPDATED_AT] ?: 0L
            )
        }
        if (cachedList.isNotEmpty() && (now - cachedAt) < CACHE_TTL_MS) {
            return cachedList
        }

        val phones = mutableListOf<String>()
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )
        cursor?.use {
            val idx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val raw = if (idx >= 0) it.getString(idx) else null
                val e164 = raw?.let { num -> phoneNumberService.normalizeToE164(num, defaultRegion) }
                if (e164 != null) phones.add(e164)
            }
        }
        val distinct = phones.distinct()
        val newHash = sha256(distinct.joinToString(","))

        // If unchanged and no valid cache, just write updated_at
        if (cachedHash != null && cachedHash == newHash && cachedList.isNotEmpty()) {
            runBlocking {
                context.contactsDataStore.edit { prefs ->
                    prefs[KEY_UPDATED_AT] = now
                }
            }
            return cachedList
        }

        // Persist cache
        runBlocking {
            context.contactsDataStore.edit { prefs ->
                prefs[KEY_HASH] = newHash
                prefs[KEY_CACHED] = distinct.joinToString(",")
                prefs[KEY_UPDATED_AT] = now
            }
        }
        return distinct
    }

    private fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}