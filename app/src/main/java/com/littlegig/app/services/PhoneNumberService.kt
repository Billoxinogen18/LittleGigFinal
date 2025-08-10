package com.littlegig.app.services

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneNumberService @Inject constructor() {
    private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    fun normalizeToE164(raw: String, defaultRegion: String = "KE"): String? {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return null
        return try {
            val proto = phoneUtil.parse(trimmed, defaultRegion)
            if (!phoneUtil.isValidNumber(proto)) return null
            phoneUtil.format(proto, PhoneNumberUtil.PhoneNumberFormat.E164)
        } catch (_: NumberParseException) {
            null
        }
    }

    fun normalizeMany(rawNumbers: List<String>, defaultRegion: String = "KE"): List<String> {
        return rawNumbers.mapNotNull { normalizeToE164(it, defaultRegion) }.distinct()
    }
}