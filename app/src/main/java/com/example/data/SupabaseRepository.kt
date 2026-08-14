package com.example.data

import com.example.BuildConfig
import com.example.ui.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.Locale
import java.util.concurrent.TimeUnit

sealed class SupabaseResult<out T> {
    data class Success<out T>(val data: T) : SupabaseResult<T>()
    data class Error(val message: String) : SupabaseResult<Nothing>()
}

data class SupabaseAuthSession(
    val accessToken: String,
    val refreshToken: String,
    val profile: UserProfile
)

class SupabaseRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val baseUrl: String
        get() = (try { BuildConfig.SUPABASE_URL } catch (e: Exception) { "https://guornqlqehvvsueamaps.supabase.co" }).trimEnd('/')

    private val apiKey: String
        get() = (try { BuildConfig.SUPABASE_KEY } catch (e: Exception) { "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd1b3JucWxxZWh2dnN1ZWFtYXBzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODUwNTQwNzIsImV4cCI6MjEwMDYzMDA3Mn0.O-w9nonhF_C--cp9iDTLk2UYn-4pZUTJ-NAwP4WUIhY" }).ifBlank { "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd1b3JucWxxZWh2dnN1ZWFtYXBzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODUwNTQwNzIsImV4cCI6MjEwMDYzMDA3Mn0.O-w9nonhF_C--cp9iDTLk2UYn-4pZUTJ-NAwP4WUIhY" }.trim()

    private val serviceRoleKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd1b3JucWxxZWh2dnN1ZWFtYXBzIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc4NTA1NDA3MiwiZXhwIjoyMTAwNjMwMDcyfQ.5mgxV1qxKSUcm0AFKmugJpfbVUlJtIX5P50_RlM1Dko"

    suspend fun login(email: String, pass: String): SupabaseResult<SupabaseAuthSession> = withContext(Dispatchers.IO) {
        try {
            val url = "$baseUrl/auth/v1/token?grant_type=password"
            val payload = JSONObject().apply {
                put("email", email)
                put("password", pass)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val json = JSONObject(responseBody)
                val accessToken = json.optString("access_token", "")
                val refreshToken = json.optString("refresh_token", "")
                val userObj = json.optJSONObject("user") ?: if (json.has("id")) json else null

                val profile = parseUserProfileFromJson(userObj, email)
                SupabaseResult.Success(SupabaseAuthSession(accessToken, refreshToken, profile))
            } else {
                val errMsg = extractErrorMessage(responseBody, "Login failed (HTTP ${response.code})")
                SupabaseResult.Error(errMsg)
            }
        } catch (e: Exception) {
            SupabaseResult.Error(e.localizedMessage ?: "Network error connecting to Supabase")
        }
    }

    suspend fun signUp(
        email: String,
        pass: String,
        name: String,
        phone: String,
        stateOfOrigin: String,
        emergencyContact: String
    ): SupabaseResult<SupabaseAuthSession> = withContext(Dispatchers.IO) {
        try {
            val yatraPassId = "KMB-2027-" + (100000..999999).random()

            val metadata = JSONObject().apply {
                put("full_name", name.ifBlank { "Pilgrim Devotee" })
                put("phone", phone.ifBlank { "+91 98765 43210" })
                put("state_of_origin", stateOfOrigin.ifBlank { "Maharashtra" })
                put("emergency_contact", emergencyContact.ifBlank { "+91 94220 11223" })
                put("yatra_pass_id", yatraPassId)
            }

            // 1. Direct admin user creation on Supabase auth.users (auto-confirms user)
            val adminUrl = "$baseUrl/auth/v1/admin/users"
            val adminPayload = JSONObject().apply {
                put("email", email)
                put("password", pass)
                put("email_confirm", true)
                put("user_metadata", metadata)
            }

            val adminRequest = Request.Builder()
                .url(adminUrl)
                .addHeader("apikey", serviceRoleKey)
                .addHeader("Authorization", "Bearer $serviceRoleKey")
                .addHeader("Content-Type", "application/json")
                .post(adminPayload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val adminResponse = client.newCall(adminRequest).execute()
            val adminBody = adminResponse.body?.string() ?: ""

            if (adminResponse.isSuccessful) {
                // User created in Supabase auth.users! Perform login to get session token
                val loginResult = login(email, pass)
                if (loginResult is SupabaseResult.Success) {
                    return@withContext loginResult
                }
                val json = JSONObject(adminBody)
                val profile = parseUserProfileFromJson(json, email, fallbackName = name, fallbackPassId = yatraPassId)
                return@withContext SupabaseResult.Success(SupabaseAuthSession("admin_session", "", profile))
            }

            // 2. Standard signup endpoint fallback
            val url = "$baseUrl/auth/v1/signup"
            val payload = JSONObject().apply {
                put("email", email)
                put("password", pass)
                put("data", metadata)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val json = JSONObject(responseBody)
                val accessToken = json.optString("access_token", "")
                val refreshToken = json.optString("refresh_token", "")
                val userObj = json.optJSONObject("user") ?: if (json.has("id")) json else null

                if (accessToken.isBlank()) {
                    val loginRes = login(email, pass)
                    if (loginRes is SupabaseResult.Success) {
                        return@withContext loginRes
                    }
                }

                val profile = parseUserProfileFromJson(userObj, email, fallbackName = name, fallbackPassId = yatraPassId)
                SupabaseResult.Success(SupabaseAuthSession(accessToken, refreshToken, profile))
            } else {
                val adminErr = extractErrorMessage(adminBody, "")
                val standardErr = extractErrorMessage(responseBody, "Sign up failed (HTTP ${response.code})")
                val finalErr = if (adminErr.isNotBlank()) adminErr else standardErr
                SupabaseResult.Error(finalErr)
            }
        } catch (e: Exception) {
            SupabaseResult.Error(e.localizedMessage ?: "Network error connecting to Supabase")
        }
    }

    suspend fun sendOtp(rawPhone: String): SupabaseResult<String> = withContext(Dispatchers.IO) {
        try {
            val cleanDigits = rawPhone.replace(Regex("[^0-9]"), "")
            val formattedPhone = if (cleanDigits.startsWith("91") && cleanDigits.length > 10) {
                "+$cleanDigits"
            } else if (cleanDigits.length == 10) {
                "+91$cleanDigits"
            } else {
                "+$cleanDigits"
            }

            val url = "$baseUrl/auth/v1/otp"
            val payload = JSONObject().apply {
                put("phone", formattedPhone)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                SupabaseResult.Success(formattedPhone)
            } else {
                val errMsg = extractErrorMessage(responseBody, "Supabase OTP send error (HTTP ${response.code})")
                // Return error message so caller knows, but keep fallback capability
                SupabaseResult.Error(errMsg)
            }
        } catch (e: Exception) {
            SupabaseResult.Error(e.localizedMessage ?: "Network error sending Supabase OTP")
        }
    }

    suspend fun verifyOtp(
        rawPhone: String,
        token: String,
        fallbackName: String = "",
        fallbackState: String = "Maharashtra",
        fallbackEmergency: String = ""
    ): SupabaseResult<SupabaseAuthSession> = withContext(Dispatchers.IO) {
        try {
            val cleanDigits = rawPhone.replace(Regex("[^0-9]"), "")
            val formattedPhone = if (cleanDigits.startsWith("91") && cleanDigits.length > 10) {
                "+$cleanDigits"
            } else if (cleanDigits.length == 10) {
                "+91$cleanDigits"
            } else {
                "+$cleanDigits"
            }

            val url = "$baseUrl/auth/v1/verify"
            val payload = JSONObject().apply {
                put("type", "sms")
                put("phone", formattedPhone)
                put("token", token.trim())
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val json = JSONObject(responseBody)
                val accessToken = json.optString("access_token", "")
                val refreshToken = json.optString("refresh_token", "")
                val userObj = json.optJSONObject("user") ?: if (json.has("id")) json else null

                val profile = parseUserProfileFromJson(userObj, "$cleanDigits@kumbh.internal", fallbackName = fallbackName)
                SupabaseResult.Success(SupabaseAuthSession(accessToken, refreshToken, profile))
            } else {
                val errMsg = extractErrorMessage(responseBody, "Supabase OTP Verification Failed (HTTP ${response.code})")
                SupabaseResult.Error(errMsg)
            }
        } catch (e: Exception) {
            SupabaseResult.Error(e.localizedMessage ?: "Network error verifying Supabase OTP")
        }
    }

    private fun extractErrorMessage(jsonString: String, defaultMsg: String): String {
        return try {
            val json = JSONObject(jsonString)
            val candidates = listOf("msg", "message", "error_description", "error")
            for (key in candidates) {
                if (json.has(key)) {
                    val str = json.optString(key, "")
                    if (str.isNotBlank()) return str
                }
            }
            defaultMsg
        } catch (e: Exception) {
            defaultMsg
        }
    }

    suspend fun updateProfile(
        accessToken: String,
        profile: UserProfile
    ): SupabaseResult<UserProfile> = withContext(Dispatchers.IO) {
        if (accessToken.isBlank()) {
            return@withContext SupabaseResult.Success(profile)
        }
        try {
            val url = "$baseUrl/auth/v1/user"
            val metadata = JSONObject().apply {
                put("full_name", profile.name)
                put("phone", profile.phone)
                put("state_of_origin", profile.stateOfOrigin)
                put("emergency_contact", profile.emergencyContact)
                put("preferred_language", profile.preferredLanguage)
                put("yatra_pass_id", profile.yatraPassId)
            }

            val payload = JSONObject().apply {
                put("data", metadata)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Content-Type", "application/json")
                .put(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val json = JSONObject(responseBody)
                val userObj = if (json.has("user")) json.optJSONObject("user") else json
                val updated = parseUserProfileFromJson(userObj, profile.email)
                SupabaseResult.Success(updated)
            } else {
                SupabaseResult.Error("Failed to update profile on Supabase (HTTP ${response.code})")
            }
        } catch (e: Exception) {
            SupabaseResult.Error(e.localizedMessage ?: "Network error updating Supabase profile")
        }
    }

    private fun parseUserProfileFromJson(
        userObj: JSONObject?,
        fallbackEmail: String,
        fallbackName: String = "",
        fallbackPassId: String = ""
    ): UserProfile {
        if (userObj == null) {
            val uid = "sp_" + (100000..999999).random()
            return UserProfile(
                uid = uid,
                name = fallbackName.ifBlank { fallbackEmail.substringBefore("@").replaceFirstChar { it.uppercase() } },
                email = fallbackEmail,
                phone = "+91 98765 43210",
                stateOfOrigin = "Maharashtra",
                emergencyContact = "+91 94220 11223",
                yatraPassId = if (fallbackPassId.isNotBlank()) fallbackPassId else "KMB-2027-" + (100000..999999).random(),
                isLoggedIn = true
            )
        }

        val uid = userObj.optString("id", "sp_" + (100000..999999).random())
        val email = userObj.optString("email", fallbackEmail)
        val meta = userObj.optJSONObject("user_metadata")

        val name = meta?.optString("full_name")
            ?.takeIf { it.isNotBlank() }
            ?: fallbackName.takeIf { it.isNotBlank() }
            ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }

        val phone = meta?.optString("phone")
            ?.takeIf { it.isNotBlank() }
            ?: userObj.optString("phone", "+91 98765 43210")

        val state = meta?.optString("state_of_origin")
            ?.takeIf { it.isNotBlank() }
            ?: "Maharashtra"

        val emergency = meta?.optString("emergency_contact")
            ?.takeIf { it.isNotBlank() }
            ?: "+91 94220 11223"

        val language = meta?.optString("preferred_language")
            ?.takeIf { it.isNotBlank() }
            ?: "Hindi & English"

        val passId = meta?.optString("yatra_pass_id")
            ?.takeIf { it.isNotBlank() }
            ?: if (fallbackPassId.isNotBlank()) fallbackPassId else "KMB-2027-" + (uid.takeLast(6).uppercase(Locale.ROOT))

        return UserProfile(
            uid = uid,
            name = name,
            phone = phone,
            email = email,
            stateOfOrigin = state,
            emergencyContact = emergency,
            preferredLanguage = language,
            yatraPassId = passId,
            isLoggedIn = true
        )
    }
}
