package com.han.konnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.han.konnect.ui.screens.ChatListScreen
import com.han.konnect.ui.screens.CorrectionNoteScreen
import com.han.konnect.ui.screens.DiscoveryScreen
import com.han.konnect.ui.screens.MyPageScreen

@Composable
fun NavigationGraph(
    navController = NavHostController,
    modifier = Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.Discovery.route,
        modifier = modifier
    ) {
        composable(NavScreen.Discovery.route) { DiscoveryScreen() }
        composable(NavScreen.Chat.route) { ChatListScreen() }
        composable(NavScreen.Correction.route) { CorrectionNoteScreen() }
        composable(NavScreen.MyPage.route) { MyPageScreen() }
    }
}