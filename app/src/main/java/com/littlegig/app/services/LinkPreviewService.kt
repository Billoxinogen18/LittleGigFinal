package com.littlegig.app.services

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LinkPreviewService @Inject constructor() {
    private val client = OkHttpClient()

    data class Preview(
        val url: String,
        val title: String?,
        val description: String?,
        val imageUrl: String?
    )

    suspend fun fetch(url: String): Preview? {
        return runCatching {
            val req = Request.Builder().url(url).header("User-Agent", "LittleGig/1.0").build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val html = resp.body?.string() ?: return null
                val title = findMeta(html, "og:title") ?: findTitle(html)
                val desc = findMeta(html, "og:description") ?: findMeta(html, "description")
                val image = findMeta(html, "og:image")
                Preview(url, title, desc, image)
            }
        }.getOrNull()
    }

    private fun findTitle(html: String): String? {
        val m = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL).matcher(html)
        return if (m.find()) m.group(1)?.trim() else null
    }

    private fun findMeta(html: String, property: String): String? {
        val pattern = "<meta[^>]+(property|name)\\s*=\\s*\"$property\"[^>]+content\\s*=\\s*\"(.*?)\"[^>]*/?>"
        val m = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE or Pattern.DOTALL).matcher(html)
        return if (m.find()) m.group(2)?.trim() else null
    }
}