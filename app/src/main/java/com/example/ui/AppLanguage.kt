package com.example.ui

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val flag: String
) {
    ENGLISH("en", "English", "English", "🇬🇧"),
    HINDI("hi", "हिंदी", "Hindi", "🇮🇳"),
    MARATHI("mr", "मराठी", "Marathi", "🇮🇳"),
    GUJARATI("gu", "ગુજરાતી", "Gujarati", "🇮🇳"),
    TAMIL("ta", "தமிழ்", "Tamil", "🇮🇳"),
    TELUGU("te", "తెలుగు", "Telugu", "🇮🇳"),
    BENGALI("bn", "বাংলা", "Bengali", "🇮🇳"),
    KANNADA("kn", "ಕನ್ನಡ", "Kannada", "🇮🇳");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return values().find { 
                it.code.equals(code, ignoreCase = true) || 
                it.name.equals(code, ignoreCase = true) || 
                it.englishName.contains(code, ignoreCase = true) ||
                it.nativeName.contains(code, ignoreCase = true)
            } ?: HINDI
        }
    }
}
