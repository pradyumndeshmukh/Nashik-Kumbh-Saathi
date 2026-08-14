package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.ui.AppLanguage
import com.example.ui.UserProfile
import com.example.ui.components.AnimatedKumbhLogo
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.SaffronPrimary
import com.example.ui.util.tr

enum class AuthMode {
    PHONE_OTP,
    EMAIL_PASSWORD
}

@Composable
fun AuthScreen(
    userProfile: UserProfile,
    isAuthLoading: Boolean = false,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onOpenDrawer: () -> Unit = {},
    onLanguageSelected: (AppLanguage) -> Unit = {},
    onLogin: (phoneOrEmail: String, pass: String) -> Unit,
    onRegister: (name: String, phone: String, email: String, pass: String, state: String, emergencyContact: String) -> Unit,
    onSendOtp: (phone: String, onSent: (String) -> Unit) -> Unit = { _, _ -> },
    onVerifyPhoneOtp: (phone: String, enteredOtp: String, expectedOtp: String, name: String, state: String, emergencyContact: String) -> Unit = { _, _, _, _, _, _ -> },
    onLogout: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Sign In, 1 = Sign Up
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row with Menu button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onOpenDrawer,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = SaffronPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Aligned Logo and Header Title
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            AnimatedKumbhLogo(
                size = 96.dp,
                showHalo = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "auth_welcome_title".tr(appLanguage),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepSaffron,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "auth_subtitle".tr(appLanguage),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Prominent App Language Selector Option right on Login Screen
        LanguageSelectorCard(
            currentLanguage = appLanguage,
            onLanguageSelected = onLanguageSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (userProfile.isLoggedIn) {
            LoggedInUserProfileCard(
                userProfile = userProfile,
                appLanguage = appLanguage,
                onLogout = onLogout,
                onNavigateToEmergency = onNavigateToEmergency
            )
        } else {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Tab Selector: Sign In vs Sign Up
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = SaffronPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    "auth_sign_in".tr(appLanguage),
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    "auth_sign_up".tr(appLanguage),
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedTab == 0) {
                        SignInForm(
                            appLanguage = appLanguage,
                            onLogin = onLogin,
                            onSendOtp = onSendOtp,
                            onVerifyPhoneOtp = onVerifyPhoneOtp
                        )
                    } else {
                        RegisterForm(
                            appLanguage = appLanguage,
                            onRegister = onRegister,
                            onSendOtp = onSendOtp,
                            onVerifyPhoneOtp = onVerifyPhoneOtp
                        )
                    }
                }

                if (isAuthLoading) {
                    Surface(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(18.dp)),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = SaffronPrimary)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Authenticating & Syncing Session...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoggedInUserProfileCard(
    userProfile: UserProfile,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onLogout: () -> Unit,
    onNavigateToEmergency: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("user_profile_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account",
                        tint = DeepSaffron,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "auth_logged_in_as".tr(appLanguage),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "LOGGED IN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailRow(icon = Icons.Default.Person, label = "auth_full_name".tr(appLanguage), value = userProfile.name)
                DetailRow(icon = Icons.Default.Phone, label = "auth_phone_number".tr(appLanguage), value = userProfile.phone.ifBlank { "Not specified" })
                DetailRow(icon = Icons.Default.Email, label = "auth_email".tr(appLanguage), value = userProfile.email)
                DetailRow(icon = Icons.Default.LocationOn, label = "auth_state".tr(appLanguage), value = userProfile.stateOfOrigin)
                DetailRow(icon = Icons.Default.ContactPhone, label = "auth_emergency_contact".tr(appLanguage), value = userProfile.emergencyContact)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("auth_logout".tr(appLanguage))
                }

                Button(
                    onClick = onNavigateToEmergency,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron)
                ) {
                    Icon(imageVector = Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("SOS Helpline")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SaffronPrimary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SignInForm(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onLogin: (phoneOrEmail: String, pass: String) -> Unit,
    onSendOtp: (phone: String, onSent: (String) -> Unit) -> Unit,
    onVerifyPhoneOtp: (phone: String, enteredOtp: String, expectedOtp: String, name: String, state: String, emergencyContact: String) -> Unit
) {
    var authMode by remember { mutableStateOf(AuthMode.PHONE_OTP) }

    // Phone OTP state
    var phone by remember { mutableStateOf("") }
    var enteredOtp by remember { mutableStateOf("") }
    var sentOtpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    // Email Password state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "auth_sign_in".tr(appLanguage),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Auth Method Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = authMode == AuthMode.PHONE_OTP,
                    onClick = { authMode = AuthMode.PHONE_OTP },
                    label = { Text("📱 " + "auth_login_with_number".tr(appLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = authMode == AuthMode.EMAIL_PASSWORD,
                    onClick = { authMode = AuthMode.EMAIL_PASSWORD },
                    label = { Text("✉️ " + "auth_login_with_email".tr(appLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (authMode == AuthMode.PHONE_OTP) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) phone = it },
                    label = { Text("auth_phone_number".tr(appLanguage)) },
                    leadingIcon = {
                        Text(
                            "+91 ",
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (!isOtpSent) {
                    Button(
                        onClick = {
                            if (phone.length >= 10) {
                                isOtpSent = true
                                onSendOtp(phone) { code ->
                                    sentOtpCode = code
                                }
                            }
                        },
                        enabled = phone.length >= 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("auth_send_otp".tr(appLanguage), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // OTP Verification Banner
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "OTP Sent successfully to +91 $phone!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) enteredOtp = it },
                        label = { Text("auth_enter_otp".tr(appLanguage)) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SaffronPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (enteredOtp.isNotBlank()) {
                                onVerifyPhoneOtp(phone, enteredOtp, sentOtpCode, "", "", "")
                            }
                        },
                        enabled = enteredOtp.length >= 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("auth_verify_otp".tr(appLanguage), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                onSendOtp(phone) { code ->
                                    sentOtpCode = code
                                }
                            }
                        ) {
                            Text("Resend OTP", fontSize = 12.sp, color = SaffronPrimary)
                        }

                        TextButton(
                            onClick = {
                                isOtpSent = false
                                enteredOtp = ""
                            }
                        ) {
                            Text("Change Phone No.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                // Email & Password Login
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { onLogin(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sign In with Email", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RegisterForm(
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onRegister: (name: String, phone: String, email: String, pass: String, state: String, emergencyContact: String) -> Unit,
    onSendOtp: (phone: String, onSent: (String) -> Unit) -> Unit,
    onVerifyPhoneOtp: (phone: String, enteredOtp: String, expectedOtp: String, name: String, state: String, emergencyContact: String) -> Unit
) {
    var authMode by remember { mutableStateOf(AuthMode.PHONE_OTP) }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("Maharashtra") }
    var emergencyContact by remember { mutableStateOf("") }

    // Phone OTP specific state
    var enteredOtp by remember { mutableStateOf("") }
    var sentOtpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    // Email specific state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "auth_sign_up".tr(appLanguage),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Auth Method Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = authMode == AuthMode.PHONE_OTP,
                    onClick = { authMode = AuthMode.PHONE_OTP },
                    label = { Text("📱 " + "auth_use_phone_otp".tr(appLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = authMode == AuthMode.EMAIL_PASSWORD,
                    onClick = { authMode = AuthMode.EMAIL_PASSWORD },
                    label = { Text("✉️ " + "auth_use_email_pass".tr(appLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Shared Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("auth_full_name".tr(appLanguage)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Phone Number Field
            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) phone = it },
                label = { Text("Mobile Phone Number") },
                leadingIcon = {
                    Text(
                        "+91 ",
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            if (authMode == AuthMode.PHONE_OTP) {
                var isOtpVerified by remember { mutableStateOf(false) }

                if (!isOtpVerified) {
                    if (!isOtpSent) {
                        Button(
                            onClick = {
                                if (phone.length >= 10) {
                                    isOtpSent = true
                                    onSendOtp(phone) { code ->
                                        sentOtpCode = code
                                    }
                                }
                            },
                            enabled = phone.length >= 10,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send OTP Verification Code", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "OTP Sent successfully to +91 $phone!",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = enteredOtp,
                            onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) enteredOtp = it },
                            label = { Text("Enter 6-Digit SMS OTP") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SaffronPrimary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                if (enteredOtp.isNotBlank()) {
                                    isOtpVerified = true
                                }
                            },
                            enabled = enteredOtp.length >= 4,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("signup_submit_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verify OTP & Proceed to Basic Details", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    onSendOtp(phone) { code ->
                                        sentOtpCode = code
                                    }
                                }
                            ) {
                                Text("Resend OTP", fontSize = 12.sp, color = SaffronPrimary)
                            }

                            TextButton(
                                onClick = {
                                    isOtpSent = false
                                    enteredOtp = ""
                                }
                            ) {
                                Text("Change Phone No.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    // BASIC DETAILS FORM PAGE (Opens after OTP verification)
                    BasicDetailsFormPage(
                        phone = phone,
                        initialName = name,
                        initialState = state,
                        initialEmergency = emergencyContact,
                        onComplete = { pilgrimName, pilgrimState, pilgrimEmergency, pilgrimEmail ->
                            onVerifyPhoneOtp(phone, enteredOtp, sentOtpCode, pilgrimName, pilgrimState, pilgrimEmergency)
                        },
                        onBackToOtp = { isOtpVerified = false }
                    )
                }
            } else {
                // Email & Password Registration
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Create Password (min 6 characters)") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State / UT of Origin") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text("Emergency Contact Number") },
                    leadingIcon = { Icon(Icons.Default.ContactPhone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { onRegister(name, phone, email, password, state, emergencyContact) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("signup_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Create Account with Email", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun BasicDetailsFormPage(
    phone: String,
    initialName: String = "",
    initialState: String = "Maharashtra",
    initialEmergency: String = "",
    onComplete: (name: String, state: String, emergencyContact: String, email: String) -> Unit,
    onBackToOtp: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var state by remember { mutableStateOf(if (initialState.isBlank()) "Maharashtra" else initialState) }
    var emergencyContact by remember { mutableStateOf(initialEmergency) }
    var email by remember { mutableStateOf("") }

    val popularStates = listOf("Maharashtra", "Madhya Pradesh", "Uttar Pradesh", "Gujarat", "Rajasthan", "Delhi")

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            color = Color(0xFFE8F5E9),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Phone Verified ✓ (+91 $phone)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "Please enter your basic details to complete registration.",
                        fontSize = 11.sp,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        Text(
            text = "Basic Pilgrim Details",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name (as per Govt ID) *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SaffronPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Column {
            Text(
                text = "State / UT of Origin *",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Quick State Selection Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                popularStates.take(3).forEach { chipState ->
                    FilterChip(
                        selected = state.equals(chipState, ignoreCase = true),
                        onClick = { state = chipState },
                        label = { Text(chipState, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        OutlinedTextField(
            value = emergencyContact,
            onValueChange = { emergencyContact = it },
            label = { Text("Emergency Helpline Contact (Family/Friend)") },
            leadingIcon = { Icon(Icons.Default.ContactPhone, contentDescription = null, tint = SaffronPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address (Optional)") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SaffronPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                onComplete(
                    if (name.isBlank()) "Pilgrim (${phone.takeLast(4)})" else name,
                    if (state.isBlank()) "Maharashtra" else state,
                    if (emergencyContact.isBlank()) "+91 98220 55443" else emergencyContact,
                    email
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_basic_details_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Details & Complete Setup", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        TextButton(
            onClick = onBackToOtp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back to OTP Verification", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun LanguageSelectorCard(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("auth_language_selector"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SaffronPrimary.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = SaffronPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Select App Language",
                                tint = DeepSaffron,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "auth_select_language".tr(currentLanguage),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${currentLanguage.flag} ${currentLanguage.nativeName} (${currentLanguage.englishName})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepSaffron
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DeepSaffron.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepSaffron,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Scrollable Chips of all languages
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(AppLanguage.values()) { lang ->
                    val isSelected = currentLanguage == lang
                    FilterChip(
                        selected = isSelected,
                        onClick = { onLanguageSelected(lang) },
                        label = {
                            Text(
                                text = "${lang.flag} ${lang.nativeName}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepSaffron,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            labelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}
