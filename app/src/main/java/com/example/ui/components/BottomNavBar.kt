package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.BottomNavTab
import com.example.ui.theme.SaffronPrimary
import com.example.ui.util.tr

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun BottomNavBar(
    selectedTab: BottomNavTab,
    appLanguage: AppLanguage = AppLanguage.ENGLISH,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        BottomNavTab.values().forEach { tab ->
            val iconVector = getIconForTab(tab)
            val isSelected = selectedTab == tab
            val tabLabelKey = when (tab) {
                BottomNavTab.HOME -> "tab_home"
                BottomNavTab.SCHEDULE -> "tab_schedule"
                BottomNavTab.MAP -> "tab_map"
                BottomNavTab.STAY -> "tab_stay"
                BottomNavTab.FOOD -> "tab_food"
            }
            val translatedLabel = tabLabelKey.tr(appLanguage)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onTabSelected(tab)
                },
                icon = {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = translatedLabel
                    )
                },
                label = {
                    Text(
                        text = translatedLabel,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SaffronPrimary,
                    selectedTextColor = SaffronPrimary,
                    indicatorColor = SaffronPrimary.copy(alpha = 0.15f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                alwaysShowLabel = true,
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

private fun getIconForTab(tab: BottomNavTab): ImageVector {
    return when (tab) {
        BottomNavTab.HOME -> Icons.Default.Home
        BottomNavTab.SCHEDULE -> Icons.Default.CalendarToday
        BottomNavTab.MAP -> Icons.Default.Map
        BottomNavTab.STAY -> Icons.Default.Hotel
        BottomNavTab.FOOD -> Icons.Default.Restaurant
    }
}
