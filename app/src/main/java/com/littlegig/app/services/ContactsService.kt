package com.littlegig.app.services

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val phoneNumberService: PhoneNumberService
) {
    fun getNormalizedPhoneContacts(defaultRegion: String = "KE"): List<String> {
        val resolver: ContentResolver = context.contentResolver
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
        return phones.distinct()
    }
}