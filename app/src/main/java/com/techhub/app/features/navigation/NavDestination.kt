package com.techhub.app.features.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CurrencyBitcoin
import androidx.compose.ui.graphics.vector.ImageVector
import com.techhub.app.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute {
    @Serializable
    data object Markets : NavRoute

    @Serializable
    data object Articles : NavRoute

    @Serializable
    data object Interview : NavRoute
}

data class BottomNavItem(
    @StringRes val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: NavRoute
)

val bottomNavItems = listOf(
    BottomNavItem(
        labelRes = R.string.nav_markets,
        selectedIcon = Icons.Filled.CurrencyBitcoin,
        unselectedIcon = Icons.Outlined.CurrencyBitcoin,
        route = NavRoute.Markets
    ),
    BottomNavItem(
        labelRes = R.string.nav_articles,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        unselectedIcon = Icons.AutoMirrored.Outlined.Article,
        route = NavRoute.Articles
    ),
    BottomNavItem(
        labelRes = R.string.nav_interview,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
        route = NavRoute.Interview
    )
)
