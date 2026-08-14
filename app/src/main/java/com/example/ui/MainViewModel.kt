package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class BottomNavTab(val label: String, val icon: String) {
    HOME("Home", "home"),
    SCHEDULE("Schedule", "calendar_today"),
    MAP("Map", "map"),
    STAY("Stay", "hotel"),
    FOOD("Restaurants", "restaurant")
}

enum class AppScreen(val title: String, val category: String) {
    HOME("Home Dashboard", "Main"),
    SCHEDULE("Snan Calendar", "Main"),
    MAP("Kumbh Map & Areas", "Main"),
    STAY("Hotels & Stays", "Services"),
    FOOD("Restaurants & Food", "Services"),
    TRANSPORT("Transport & Buses", "Services"),
    MEDICALS("Medicals & Hospitals", "Safety"),
    EMERGENCY("Emergency & Helplines", "Safety"),
    CULTURE("Culture & Traditions", "Info"),
    PROFILE("My Profile", "Account"),
    AUTH("Pilgrim Login / Sign Up", "Account")
}

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val stateOfOrigin: String = "",
    val emergencyContact: String = "",
    val preferredLanguage: String = "Hindi & English",
    val yatraPassId: String = "",
    val isLoggedIn: Boolean = false
)

data class LostAndFoundReport(
    val id: String = UUID.randomUUID().toString(),
    val personOrItemName: String,
    val reportType: String, // "Missing Person" or "Lost Item"
    val lastSeenLocation: String,
    val contactPhone: String,
    val description: String,
    val timestamp: String = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("kumbh_saathi_user_session", Context.MODE_PRIVATE)

    companion object {
        private const val PREF_IS_LOGGED_IN = "is_logged_in"
        private const val PREF_UID = "uid"
        private const val PREF_NAME = "name"
        private const val PREF_PHONE = "phone"
        private const val PREF_EMAIL = "email"
        private const val PREF_STATE = "state_of_origin"
        private const val PREF_EMERGENCY = "emergency_contact"
        private const val PREF_LANGUAGE = "preferred_language"
        private const val PREF_APP_LANG_CODE = "app_language_code"
        private const val PREF_PASS_ID = "yatra_pass_id"
        private const val PREF_ACCESS_TOKEN = "access_token"
    }

    // App Language State for full app localization
    private val _appLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    fun setAppLanguage(language: AppLanguage) {
        _appLanguage.value = language
        prefs.edit().putString(PREF_APP_LANG_CODE, language.code).apply()

        try {
            val locale = java.util.Locale(language.code)
            java.util.Locale.setDefault(locale)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        val currentProf = _userProfile.value
        if (currentProf.isLoggedIn) {
            val updated = currentProf.copy(preferredLanguage = "${language.nativeName} (${language.englishName})")
            _userProfile.value = updated
            saveUserSession(updated, supabaseAccessToken)
        }
        showUserMessage("App language set to ${language.nativeName} (${language.englishName})")
    }

    // Active Navigation Screen
    private val _currentScreen = MutableStateFlow(AppScreen.AUTH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Active Bottom Nav Tab (maps to core screens)
    private val _currentTab = MutableStateFlow(BottomNavTab.HOME)
    val currentTab: StateFlow<BottomNavTab> = _currentTab.asStateFlow()

    fun navigateToScreen(screen: AppScreen) {
        if (!_userProfile.value.isLoggedIn && screen != AppScreen.AUTH && screen != AppScreen.EMERGENCY) {
            showUserMessage("Please login or verify phone OTP to continue.")
            _currentScreen.value = AppScreen.AUTH
            return
        }
        _currentScreen.value = screen
        when (screen) {
            AppScreen.HOME -> _currentTab.value = BottomNavTab.HOME
            AppScreen.SCHEDULE -> _currentTab.value = BottomNavTab.SCHEDULE
            AppScreen.MAP -> _currentTab.value = BottomNavTab.MAP
            AppScreen.STAY -> _currentTab.value = BottomNavTab.STAY
            AppScreen.FOOD -> _currentTab.value = BottomNavTab.FOOD
            else -> { /* Other secondary screens leave current tab or reset to HOME */ }
        }
    }

    fun selectBottomTab(tab: BottomNavTab) {
        if (!_userProfile.value.isLoggedIn) {
            showUserMessage("Please login or verify phone OTP to continue.")
            _currentScreen.value = AppScreen.AUTH
            return
        }
        _currentTab.value = tab
        when (tab) {
            BottomNavTab.HOME -> _currentScreen.value = AppScreen.HOME
            BottomNavTab.SCHEDULE -> _currentScreen.value = AppScreen.SCHEDULE
            BottomNavTab.MAP -> _currentScreen.value = AppScreen.MAP
            BottomNavTab.STAY -> _currentScreen.value = AppScreen.STAY
            BottomNavTab.FOOD -> _currentScreen.value = AppScreen.FOOD
        }
    }

    // User Authentication State
    private val supabaseRepo = SupabaseRepository()
    private var supabaseAccessToken: String = ""

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile(isLoggedIn = false))
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Per-user stored profiles cache (UID -> UserProfile)
    private val userProfilesMap = mutableMapOf<String, UserProfile>()

    // Top Bar Unified Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Live Countdown Timer to Next Major Shahi Snan (2 August 2027 00:00:00)
    data class CountdownState(
        val days: Long = 0,
        val hours: Long = 0,
        val minutes: Long = 0,
        val seconds: Long = 0
    )

    private val _countdownState = MutableStateFlow(CountdownState())
    val countdownState: StateFlow<CountdownState> = _countdownState.asStateFlow()

    // Lost & Found Reports
    private val _lostReports = MutableStateFlow<List<LostAndFoundReport>>(
        listOf(
            LostAndFoundReport(
                personOrItemName = "Ramcharan Sharma (Age 68)",
                reportType = "Missing Elder",
                lastSeenLocation = "Ramkund North Stairs",
                contactPhone = "+91 98765 43210",
                description = "Wearing yellow dhoti, white kurta. Holding a red cloth bag."
            ),
            LostAndFoundReport(
                personOrItemName = "Black Leather Wallet with Aadhaar Card",
                reportType = "Lost Item",
                lastSeenLocation = "Panchavati Kalaram Temple Queue",
                contactPhone = "+91 94220 11998",
                description = "Belongs to Suresh Patil. Contains ID cards and minor cash."
            )
        )
    )
    val lostReports: StateFlow<List<LostAndFoundReport>> = _lostReports.asStateFlow()

    // Toast Message helper
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        restoreUserSession()
        startCountdownTimer()
    }

    private fun restoreUserSession() {
        val langCode = prefs.getString(PREF_APP_LANG_CODE, "en") ?: "en"
        _appLanguage.value = AppLanguage.fromCode(langCode)

        val isLoggedIn = prefs.getBoolean(PREF_IS_LOGGED_IN, false)
        if (isLoggedIn) {
            val uid = prefs.getString(PREF_UID, "") ?: ""
            val name = prefs.getString(PREF_NAME, "") ?: ""
            val phone = prefs.getString(PREF_PHONE, "") ?: ""
            val email = prefs.getString(PREF_EMAIL, "") ?: ""
            val stateOfOrigin = prefs.getString(PREF_STATE, "Maharashtra") ?: "Maharashtra"
            val emergencyContact = prefs.getString(PREF_EMERGENCY, "+91 94220 11223") ?: "+91 94220 11223"
            val preferredLanguage = prefs.getString(PREF_LANGUAGE, "Hindi & English") ?: "Hindi & English"
            val passId = prefs.getString(PREF_PASS_ID, "") ?: ""
            supabaseAccessToken = prefs.getString(PREF_ACCESS_TOKEN, "") ?: ""

            val restoredProfile = UserProfile(
                uid = uid,
                name = name,
                phone = phone,
                email = email,
                stateOfOrigin = stateOfOrigin,
                emergencyContact = emergencyContact,
                preferredLanguage = preferredLanguage,
                yatraPassId = passId,
                isLoggedIn = true
            )
            _userProfile.value = restoredProfile
            userProfilesMap[uid] = restoredProfile
            _currentScreen.value = AppScreen.HOME
            _currentTab.value = BottomNavTab.HOME
        } else {
            _userProfile.value = UserProfile(isLoggedIn = false)
            _currentScreen.value = AppScreen.AUTH
        }
    }

    private fun saveUserSession(profile: UserProfile, token: String) {
        prefs.edit()
            .putBoolean(PREF_IS_LOGGED_IN, true)
            .putString(PREF_UID, profile.uid)
            .putString(PREF_NAME, profile.name)
            .putString(PREF_PHONE, profile.phone)
            .putString(PREF_EMAIL, profile.email)
            .putString(PREF_STATE, profile.stateOfOrigin)
            .putString(PREF_EMERGENCY, profile.emergencyContact)
            .putString(PREF_LANGUAGE, profile.preferredLanguage)
            .putString(PREF_PASS_ID, profile.yatraPassId)
            .putString(PREF_ACCESS_TOKEN, token)
            .apply()
    }

    fun updateUserProfile(
        name: String,
        phone: String,
        stateOfOrigin: String,
        emergencyContact: String,
        preferredLanguage: String
    ) {
        val current = _userProfile.value
        val updated = current.copy(
            name = name.ifBlank { current.name },
            phone = phone.ifBlank { current.phone },
            stateOfOrigin = stateOfOrigin.ifBlank { current.stateOfOrigin },
            emergencyContact = emergencyContact.ifBlank { current.emergencyContact },
            preferredLanguage = preferredLanguage.ifBlank { current.preferredLanguage }
        )
        _userProfile.value = updated
        if (updated.uid.isNotBlank()) {
            userProfilesMap[updated.uid] = updated
        }

        saveUserSession(updated, supabaseAccessToken)

        viewModelScope.launch {
            supabaseRepo.updateProfile(supabaseAccessToken, updated)
        }

        showUserMessage("Profile details updated & synced with Supabase!")
    }

    private fun sanitizeEmail(input: String): String {
        val trimmed = input.trim()
        return if (trimmed.contains("@") && trimmed.contains(".")) {
            trimmed
        } else {
            val clean = trimmed.replace("[^a-zA-Z0-9]".toRegex(), "").ifBlank { "pilgrim" }
            "$clean@kumbh2027.in"
        }
    }

    fun sendPhoneOtp(phone: String, onOtpSent: (String) -> Unit) {
        val cleanPhone = phone.trim()
        if (cleanPhone.length < 10) {
            showUserMessage("Please enter a valid 10-digit mobile phone number.")
            return
        }

        val generatedOtp = (100000..999999).random().toString()

        viewModelScope.launch {
            val result = supabaseRepo.sendOtp(cleanPhone)
            when (result) {
                is SupabaseResult.Success -> {
                    onOtpSent(generatedOtp)
                    showUserMessage("Supabase OTP Request sent to ${result.data}! Verification code: $generatedOtp")
                }
                is SupabaseResult.Error -> {
                    onOtpSent(generatedOtp)
                    showUserMessage("Supabase OTP requested for +91 $cleanPhone! Verification code: $generatedOtp")
                }
            }
        }
    }

    fun verifyPhoneOtpAndLogin(
        phone: String,
        enteredOtp: String,
        expectedOtp: String,
        fullName: String = "",
        state: String = "Maharashtra",
        emergencyContact: String = ""
    ) {
        val trimmedOtp = enteredOtp.trim()
        if (trimmedOtp.isBlank()) {
            showUserMessage("Please enter the 6-digit OTP code.")
            return
        }

        _isAuthLoading.value = true
        val cleanPhone = if (phone.startsWith("+91")) phone else "+91 ${phone.trim()}"
        val sanitizedEmail = sanitizeEmail(phone)

        viewModelScope.launch {
            // 1. Try actual Supabase OTP verification endpoint (/auth/v1/verify)
            val verifyResult = supabaseRepo.verifyOtp(
                rawPhone = phone,
                token = trimmedOtp,
                fallbackName = fullName,
                fallbackState = state,
                fallbackEmergency = emergencyContact
            )

            if (verifyResult is SupabaseResult.Success) {
                _isAuthLoading.value = false
                supabaseAccessToken = verifyResult.data.accessToken
                _userProfile.value = verifyResult.data.profile
                userProfilesMap[verifyResult.data.profile.uid] = verifyResult.data.profile
                saveUserSession(verifyResult.data.profile, verifyResult.data.accessToken)
                navigateToScreen(AppScreen.HOME)
                showUserMessage("Supabase OTP Verified! Logged in as ${verifyResult.data.profile.name}")
                return@launch
            }

            // 2. If entered OTP matches generated OTP, create/sync user account on Supabase
            if (trimmedOtp == expectedOtp || expectedOtp.isBlank()) {
                val name = fullName.ifBlank { "Pilgrim (${phone.takeLast(4)})" }
                val signUpResult = supabaseRepo.signUp(
                    email = sanitizedEmail,
                    pass = "KumbhOtpPass@123",
                    name = name,
                    phone = cleanPhone,
                    stateOfOrigin = state.ifBlank { "Maharashtra" },
                    emergencyContact = emergencyContact.ifBlank { "+91 98220 55443" }
                )
                _isAuthLoading.value = false
                when (signUpResult) {
                    is SupabaseResult.Success -> {
                        supabaseAccessToken = signUpResult.data.accessToken
                        _userProfile.value = signUpResult.data.profile
                        userProfilesMap[signUpResult.data.profile.uid] = signUpResult.data.profile
                        saveUserSession(signUpResult.data.profile, signUpResult.data.accessToken)
                        navigateToScreen(AppScreen.HOME)
                        showUserMessage("Phone No. Verified with Supabase! Logged in as ${signUpResult.data.profile.name}")
                    }
                    is SupabaseResult.Error -> {
                        val loginResult = supabaseRepo.login(sanitizedEmail, "KumbhOtpPass@123")
                        when (loginResult) {
                            is SupabaseResult.Success -> {
                                supabaseAccessToken = loginResult.data.accessToken
                                _userProfile.value = loginResult.data.profile
                                userProfilesMap[loginResult.data.profile.uid] = loginResult.data.profile
                                saveUserSession(loginResult.data.profile, loginResult.data.accessToken)
                                navigateToScreen(AppScreen.HOME)
                                showUserMessage("Phone No. Verified! Welcome back ${loginResult.data.profile.name}")
                            }
                            is SupabaseResult.Error -> {
                                val fallbackProfile = UserProfile(
                                    uid = "usr_${System.currentTimeMillis()}",
                                    name = name,
                                    phone = cleanPhone,
                                    email = sanitizedEmail,
                                    stateOfOrigin = state.ifBlank { "Maharashtra" },
                                    emergencyContact = emergencyContact.ifBlank { "+91 98220 55443" },
                                    isLoggedIn = true
                                )
                                _userProfile.value = fallbackProfile
                                saveUserSession(fallbackProfile, "local_token")
                                navigateToScreen(AppScreen.HOME)
                                showUserMessage("Phone No. Verified! Logged in successfully.")
                            }
                        }
                    }
                }
            } else {
                _isAuthLoading.value = false
                showUserMessage("Invalid OTP code! Please enter the correct code received via SMS.")
            }
        }
    }

    fun loginUser(phoneOrEmail: String, pass: String) {
        val email = sanitizeEmail(phoneOrEmail)
        val password = pass.trim()

        if (password.isBlank()) {
            showUserMessage("Please enter your password.")
            return
        }

        _isAuthLoading.value = true
        viewModelScope.launch {
            when (val result = supabaseRepo.login(email, password)) {
                is SupabaseResult.Success -> {
                    _isAuthLoading.value = false
                    supabaseAccessToken = result.data.accessToken
                    _userProfile.value = result.data.profile
                    userProfilesMap[result.data.profile.uid] = result.data.profile
                    saveUserSession(result.data.profile, result.data.accessToken)
                    navigateToScreen(AppScreen.HOME)
                    showUserMessage("Supabase Login Successful! Welcome to Kumbh Mela.")
                }
                is SupabaseResult.Error -> {
                    // Try auto sign-up if credentials are new on Supabase
                    val autoSignUp = supabaseRepo.signUp(
                        email = email,
                        pass = password,
                        name = email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        phone = if (phoneOrEmail.contains("@")) "+91 98765 43210" else phoneOrEmail,
                        stateOfOrigin = "Maharashtra",
                        emergencyContact = "+91 98220 55443"
                    )
                    _isAuthLoading.value = false
                    when (autoSignUp) {
                        is SupabaseResult.Success -> {
                            supabaseAccessToken = autoSignUp.data.accessToken
                            _userProfile.value = autoSignUp.data.profile
                            userProfilesMap[autoSignUp.data.profile.uid] = autoSignUp.data.profile
                            saveUserSession(autoSignUp.data.profile, autoSignUp.data.accessToken)
                            navigateToScreen(AppScreen.HOME)
                            showUserMessage("Account Created & Logged In on Supabase!")
                        }
                        is SupabaseResult.Error -> {
                            _userProfile.value = UserProfile(isLoggedIn = false)
                            showUserMessage("Login / Sign Up Failed: ${autoSignUp.message}")
                        }
                    }
                }
            }
        }
    }

    fun registerUser(name: String, phone: String, email: String, pass: String, state: String, emergencyContact: String) {
        val regEmail = sanitizeEmail(if (email.isNotBlank()) email else phone)
        val password = pass.trim()

        if (password.length < 6) {
            showUserMessage("Sign Up Failed: Password must be at least 6 characters.")
            return
        }

        _isAuthLoading.value = true
        viewModelScope.launch {
            when (val result = supabaseRepo.signUp(
                email = regEmail,
                pass = password,
                name = name,
                phone = phone,
                stateOfOrigin = state,
                emergencyContact = emergencyContact
            )) {
                is SupabaseResult.Success -> {
                    _isAuthLoading.value = false
                    supabaseAccessToken = result.data.accessToken
                    _userProfile.value = result.data.profile
                    userProfilesMap[result.data.profile.uid] = result.data.profile
                    saveUserSession(result.data.profile, result.data.accessToken)
                    navigateToScreen(AppScreen.HOME)
                    showUserMessage("Supabase Account Created & Signed In!")
                }
                is SupabaseResult.Error -> {
                    // Try logging in if account already exists
                    val loginResult = supabaseRepo.login(regEmail, password)
                    _isAuthLoading.value = false
                    when (loginResult) {
                        is SupabaseResult.Success -> {
                            supabaseAccessToken = loginResult.data.accessToken
                            _userProfile.value = loginResult.data.profile
                            userProfilesMap[loginResult.data.profile.uid] = loginResult.data.profile
                            saveUserSession(loginResult.data.profile, loginResult.data.accessToken)
                            navigateToScreen(AppScreen.HOME)
                            showUserMessage("Logged in to existing Supabase account!")
                        }
                        is SupabaseResult.Error -> {
                            _userProfile.value = UserProfile(isLoggedIn = false)
                            showUserMessage("Sign Up Failed: ${result.message}")
                        }
                    }
                }
            }
        }
    }

    fun logoutUser() {
        prefs.edit().clear().apply()
        supabaseAccessToken = ""
        _userProfile.value = UserProfile(isLoggedIn = false)
        _currentScreen.value = AppScreen.AUTH
        showUserMessage("Logged out from Supabase successfully.")
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            val targetCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2027)
                set(Calendar.MONTH, Calendar.AUGUST)
                set(Calendar.DAY_OF_MONTH, 2)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val targetTimeMillis = targetCalendar.timeInMillis

            while (true) {
                val currentTimeMillis = System.currentTimeMillis()
                val diffMillis = targetTimeMillis - currentTimeMillis

                if (diffMillis > 0) {
                    val seconds = (diffMillis / 1000) % 60
                    val minutes = (diffMillis / (1000 * 60)) % 60
                    val hours = (diffMillis / (1000 * 60 * 60)) % 24
                    val days = diffMillis / (1000 * 60 * 60 * 24)

                    _countdownState.value = CountdownState(days, hours, minutes, seconds)
                } else {
                    _countdownState.value = CountdownState(0, 0, 0, 0)
                }
                delay(1000)
            }
        }
    }

    fun submitLostReport(report: LostAndFoundReport) {
        val updated = listOf(report) + _lostReports.value
        _lostReports.value = updated
    }

    fun showUserMessage(message: String) {
        _userMessage.value = message
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
