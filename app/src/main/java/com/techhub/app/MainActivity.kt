package com.techhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techhub.app.core.designsystem.components.ThemeSelectorSheet
import com.techhub.app.core.designsystem.theme.TechHubTheme
import com.techhub.app.core.designsystem.theme.ThemeManager
import com.techhub.app.core.designsystem.theme.ThemeMode
import com.techhub.app.features.navigation.TechHubNavGraph
import com.techhub.app.features.navigation.bottomNavItems
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState by themeManager.themeState.collectAsStateWithLifecycle()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (themeState.mode) {
                ThemeMode.SYSTEM -> isSystemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // Dynamically synchronize system status bar icons and transparency with theme
            DisposableEffect(isDark) {
                enableEdgeToEdge(
                    statusBarStyle = if (isDark) {
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    },
                    navigationBarStyle = if (isDark) {
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    }
                )
                onDispose {}
            }

            TechHubTheme(
                palette = themeState.palette,
                themeMode = themeState.mode
            ) {
                MainAppScreen(
                    themeManager = themeManager
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    themeManager: ThemeManager
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val themeState by themeManager.themeState.collectAsStateWithLifecycle()
    var showThemeSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Zero outer insets so child screens' TopAppBar extends directly under the status bar without double padding
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.hasRoute(item.route::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = stringResource(id = item.labelRes)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = item.labelRes),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        TechHubNavGraph(
            navController = navController,
            onOpenThemeSettings = { showThemeSheet = true },
            // Only apply bottom bar padding to the content; top bar handles status bar insets directly
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )

        if (showThemeSheet) {
            ThemeSelectorSheet(
                themeState = themeState,
                onPaletteSelect = { themeManager.updatePalette(it) },
                onModeSelect = { themeManager.updateMode(it) },
                sheetState = sheetState,
                onDismissRequest = { showThemeSheet = false }
            )
        }
    }
}
