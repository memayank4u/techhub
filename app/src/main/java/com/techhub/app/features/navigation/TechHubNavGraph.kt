package com.techhub.app.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.techhub.app.features.articles.presentation.ArticlesScreen
import com.techhub.app.features.crypto.presentation.CryptoScreen
import com.techhub.app.features.interview.presentation.InterviewScreen

@Composable
fun TechHubNavGraph(
    navController: NavHostController,
    onOpenThemeSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Markets,
        modifier = modifier
    ) {
        composable<NavRoute.Markets> {
            CryptoScreen(onOpenThemeSettings = onOpenThemeSettings)
        }
        composable<NavRoute.Articles> {
            ArticlesScreen(onOpenThemeSettings = onOpenThemeSettings)
        }
        composable<NavRoute.Interview> {
            InterviewScreen(onOpenThemeSettings = onOpenThemeSettings)
        }
    }
}
