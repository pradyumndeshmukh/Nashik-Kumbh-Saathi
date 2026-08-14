package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.KumbhDataRepository
import com.example.ui.AppScreen
import com.example.ui.BottomNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.DeepSaffron
import com.example.ui.theme.NashikKumbhSaathiTheme
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.launch

import com.example.ui.util.tr
import com.example.ui.AppLanguage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NashikKumbhSaathiTheme {
                KumbhSaathiMainApp()
            }
        }
    }
}

@Composable
fun KumbhSaathiMainApp(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val countdownState by viewModel.countdownState.collectAsStateWithLifecycle()
    val currentAdvisory by KumbhDataRepository.currentCrowdAdvisory.collectAsStateWithLifecycle()
    val reminderIds by KumbhDataRepository.reminderEventIds.collectAsStateWithLifecycle()
    val lostReports by viewModel.lostReports.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isAuthLoading by viewModel.isAuthLoading.collectAsStateWithLifecycle()

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearUserMessage()
        }
    }

    Crossfade(
        targetState = showSplash,
        animationSpec = tween(durationMillis = 600),
        label = "splash_crossfade"
    ) { isSplash ->
        if (isSplash) {
            SplashScreen(
                onSplashFinished = { showSplash = false }
            )
        } else {
            // Handle Back Button: close drawer first, or navigate to HOME if on sub-screen
            BackHandler(enabled = drawerState.isOpen) {
                coroutineScope.launch { drawerState.close() }
            }
            BackHandler(enabled = !drawerState.isOpen && currentScreen != AppScreen.HOME) {
                viewModel.navigateToScreen(AppScreen.HOME)
            }

            ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = true,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 24.dp)
                        ) {
                            // Drawer Header (Pilgrim Pass Badge Summary)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SaffronPrimary)
                                    .clickable {
                                        viewModel.navigateToScreen(if (userProfile.isLoggedIn) AppScreen.PROFILE else AppScreen.AUTH)
                                        coroutineScope.launch { drawerState.close() }
                                    }
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color.White,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Badge,
                                                    contentDescription = null,
                                                    tint = DeepSaffron,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                        }

                                        Surface(
                                            color = Color.White.copy(alpha = 0.25f),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = if (userProfile.isLoggedIn) "VERIFIED DEVOTEE" else "GUEST",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = if (userProfile.isLoggedIn) userProfile.name else "Pilgrim Devotee",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Text(
                                        text = if (userProfile.isLoggedIn) "Phone: ${userProfile.phone.ifBlank { userProfile.email }}" else "Tap to login or register account",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "MAIN MENU",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )

                            // Navigation Drawer Options
                            AppScreen.values().forEach { screen ->
                                val screenKey = when (screen) {
                                    AppScreen.HOME -> "tab_home"
                                    AppScreen.SCHEDULE -> "tab_schedule"
                                    AppScreen.MAP -> "tab_map"
                                    AppScreen.STAY -> "tab_stay"
                                    AppScreen.FOOD -> "tab_food"
                                    AppScreen.TRANSPORT -> "nav_transport"
                                    AppScreen.MEDICALS -> "nav_medical"
                                    AppScreen.EMERGENCY -> "nav_emergency"
                                    AppScreen.CULTURE -> "nav_culture"
                                    AppScreen.PROFILE -> "nav_profile"
                                    AppScreen.AUTH -> "auth_sign_in"
                                }
                                NavigationDrawerItem(
                                    label = { Text(screenKey.tr(appLanguage), fontWeight = if (currentScreen == screen) FontWeight.Bold else FontWeight.Medium) },
                                    icon = { Icon(getIconForScreen(screen), contentDescription = null, tint = if (currentScreen == screen) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant) },
                                    selected = currentScreen == screen,
                                    onClick = {
                                        viewModel.navigateToScreen(screen)
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = SaffronPrimary.copy(alpha = 0.12f),
                                        selectedIconColor = SaffronPrimary,
                                        selectedTextColor = SaffronPrimary
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.navigateToScreen(AppScreen.AUTH)
                                        coroutineScope.launch { drawerState.close() }
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = DeepSaffron)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (userProfile.isLoggedIn) "View Profile Account" else "Login / Sign Up Account",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepSaffron
                                )
                            }
                        }
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (userProfile.isLoggedIn) {
                            BottomNavBar(
                                selectedTab = currentTab,
                                appLanguage = appLanguage,
                                onTabSelected = { tab ->
                                    viewModel.selectBottomTab(tab)
                                }
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                            when (screen) {
                                AppScreen.HOME -> HomeScreen(
                                    countdownState = countdownState,
                                    currentAdvisory = currentAdvisory,
                                    onAdvisoryChanged = { KumbhDataRepository.updateCrowdAdvisory(it) },
                                    searchQuery = searchQuery,
                                    onQueryChanged = { viewModel.updateSearchQuery(it) },
                                    onNavigateToScreen = { viewModel.navigateToScreen(it) },
                                    onOpenCulture = { viewModel.navigateToScreen(AppScreen.CULTURE) },
                                    onToggleReminder = { id ->
                                        KumbhDataRepository.toggleReminder(id)
                                        val isNowSet = KumbhDataRepository.reminderEventIds.value.contains(id)
                                        viewModel.showUserMessage(if (isNowSet) "Reminder set for Snan event!" else "Reminder removed.")
                                    },
                                    reminderIds = reminderIds,
                                    userProfile = userProfile,
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.SCHEDULE -> ScheduleScreen(
                                    reminderIds = reminderIds,
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                                    onToggleReminder = { id ->
                                        KumbhDataRepository.toggleReminder(id)
                                        val isNowSet = KumbhDataRepository.reminderEventIds.value.contains(id)
                                        viewModel.showUserMessage(if (isNowSet) "Event added to your calendar reminders!" else "Reminder removed.")
                                    }
                                )

                                AppScreen.MAP -> MapScreen(
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.STAY -> StayScreen(
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.FOOD -> FoodScreen(
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.TRANSPORT -> TransportScreen(
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.MEDICALS -> MedicalScreen(
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                                    onShowToast = { msg -> viewModel.showUserMessage(msg) }
                                )

                                AppScreen.EMERGENCY -> EmergencyScreen(
                                    lostReports = lostReports,
                                    onSubmitLostReport = { report -> viewModel.submitLostReport(report) },
                                    onShowToast = { msg -> viewModel.showUserMessage(msg) },
                                    onNavigateToMedicals = { viewModel.navigateToScreen(AppScreen.MEDICALS) },
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.CULTURE -> CultureScreen(
                                    onBack = { viewModel.navigateToScreen(AppScreen.HOME) },
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.PROFILE -> ProfileScreen(
                                    userProfile = userProfile,
                                    savedRemindersCount = reminderIds.size,
                                    lostFoundReportsCount = lostReports.size,
                                    onUpdateProfile = { name, phone, state, emergency, lang ->
                                        viewModel.updateUserProfile(name, phone, state, emergency, lang)
                                    },
                                    onLogout = { viewModel.logoutUser() },
                                    onNavigateToAuth = { viewModel.navigateToScreen(AppScreen.AUTH) },
                                    onNavigateToEmergency = { viewModel.navigateToScreen(AppScreen.EMERGENCY) },
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                )

                                AppScreen.AUTH -> AuthScreen(
                                    userProfile = userProfile,
                                    isAuthLoading = isAuthLoading,
                                    appLanguage = appLanguage,
                                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                                    onLanguageSelected = { viewModel.setAppLanguage(it) },
                                    onLogin = { phoneOrEmail, pass -> viewModel.loginUser(phoneOrEmail, pass) },
                                    onRegister = { name, phone, email, pass, state, emergencyContact ->
                                        viewModel.registerUser(name, phone, email, pass, state, emergencyContact)
                                    },
                                    onSendOtp = { phone, onSent -> viewModel.sendPhoneOtp(phone, onSent) },
                                    onVerifyPhoneOtp = { phone, enteredOtp, expectedOtp, name, state, emergencyContact ->
                                        viewModel.verifyPhoneOtpAndLogin(phone, enteredOtp, expectedOtp, name, state, emergencyContact)
                                    },
                                    onLogout = { viewModel.logoutUser() },
                                    onNavigateToEmergency = { viewModel.navigateToScreen(AppScreen.EMERGENCY) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getIconForScreen(screen: AppScreen): ImageVector {
    return when (screen) {
        AppScreen.HOME -> Icons.Default.Home
        AppScreen.SCHEDULE -> Icons.Default.CalendarToday
        AppScreen.MAP -> Icons.Default.Map
        AppScreen.STAY -> Icons.Default.Hotel
        AppScreen.FOOD -> Icons.Default.Restaurant
        AppScreen.TRANSPORT -> Icons.Default.DirectionsBus
        AppScreen.MEDICALS -> Icons.Default.MedicalServices
        AppScreen.EMERGENCY -> Icons.Default.Emergency
        AppScreen.CULTURE -> Icons.Default.AutoAwesome
        AppScreen.PROFILE -> Icons.Default.AccountCircle
        AppScreen.AUTH -> Icons.Default.Badge
    }
}
