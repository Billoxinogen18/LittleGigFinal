package com.littlegig.app.services

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.littlegig.app.R
import timber.log.Timber

@Singleton
class PlacesService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val httpClient = OkHttpClient()
    
    data class PlaceSuggestion(
        val placeId: String,
        val description: String,
        val mainText: String,
        val secondaryText: String
    )
    
    data class PlaceDetails(
        val placeId: String,
        val name: String,
        val address: String,
        val latLng: LatLng,
        val formattedAddress: String
    )

    data class PlacesError(
        val status: String,
        val message: String
    )

    private fun getApiKey(): String = context.getString(R.string.google_places_key)

    private fun isKeyLikelyInvalid(key: String): Boolean {
        return key.isBlank() || key.contains("YOUR_API_KEY", ignoreCase = true)
    }

    suspend fun verifyKeyAndBilling(): Result<Unit> = withContext(Dispatchers.IO) {
        val key = getApiKey()
        if (isKeyLikelyInvalid(key)) {
            return@withContext Result.failure(Exception("Google Places API key is missing or invalid"))
        }
        return@withContext try {
            val pingUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=little&key=$key"
            val response = httpClient.newCall(Request.Builder().url(pingUrl).build()).execute()
            val body = response.body?.string().orEmpty()
            val json = JSONObject(body)
            val status = json.optString("status", "UNKNOWN_ERROR")
            if (status == "OK" || status == "ZERO_RESULTS") {
                Result.success(Unit)
            } else {
                val errorMessage = json.optString("error_message", status)
                Timber.w("Places verify failed: $status - $errorMessage")
                Result.failure(Exception("Places billing/key issue: $status"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Places verify exception")
            Result.failure(e)
        }
    }
    
    suspend fun getPlaceSuggestions(query: String): List<PlaceSuggestion> = withContext(Dispatchers.IO) {
        if (query.length < 3) return@withContext emptyList()
        try {
            val key = getApiKey()
            val url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                java.net.URLEncoder.encode(query, "UTF-8") + "&key=" + key + "&types=establishment"
            val request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()
            val body = response.body?.string() ?: return@withContext emptyList()
            val json = JSONObject(body)
            val status = json.optString("status", "OK")
            if (status != "OK") {
                Timber.w("Places suggestions status: $status - ${json.optString("error_message", "")} ")
                return@withContext emptyList()
            }
            val predictions = json.optJSONArray("predictions") ?: return@withContext emptyList()
            val list = mutableListOf<PlaceSuggestion>()
            for (i in 0 until predictions.length()) {
                val p = predictions.getJSONObject(i)
                val placeId = p.optString("place_id")
                val description = p.optString("description")
                val structured = p.optJSONObject("structured_formatting")
                val mainText = structured?.optString("main_text") ?: description
                val secondaryText = structured?.optString("secondary_text") ?: ""
                list.add(
                    PlaceSuggestion(
                        placeId = placeId,
                        description = description,
                        mainText = mainText,
                        secondaryText = secondaryText
                    )
                )
            }
            list
        } catch (e: Exception) {
            Timber.e(e, "Places suggestions error")
            emptyList()
        }
    }
    
    suspend fun getPlaceDetails(placeId: String): PlaceDetails? = withContext(Dispatchers.IO) {
        try {
            val key = getApiKey()
            val url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" +
                java.net.URLEncoder.encode(placeId, "UTF-8") +
                "&key=" + key + "&fields=name,formatted_address,geometry"
            val request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null
            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)
            val status = json.optString("status", "OK")
            if (status != "OK") {
                Timber.w("Places details status: $status - ${json.optString("error_message", "")}")
                return@withContext null
            }
            val result = json.optJSONObject("result") ?: return@withContext null
            val name = result.optString("name")
            val formatted = result.optString("formatted_address")
            val geometry = result.optJSONObject("geometry")
            val location = geometry?.optJSONObject("location")
            val lat = location?.optDouble("lat") ?: return@withContext null
            val lng = location.optDouble("lng")
            PlaceDetails(
                placeId = placeId,
                name = name,
                address = formatted,
                latLng = LatLng(lat, lng),
                formattedAddress = formatted
            )
        } catch (e: Exception) {
            Timber.e(e, "Places details error")
            null
        }
    }
    
    fun clearSuggestions() {
        // This is handled by the UI state management
    }
} 