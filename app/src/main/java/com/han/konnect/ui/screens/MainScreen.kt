package com.han.konnect.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.han.konnect.ui.navigation.Screen
import com.han.konnect.ui.theme.PurpleMain

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Matching,
        Screen.ChatList,
        Screen.Corrections,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Text(screen.icon, fontSize = 20.sp) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PurpleMain,
                            selectedTextColor = PurpleMain,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = PurpleMain.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Matching.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. 파트너 찾기 (매칭) 탭
            composable(Screen.Matching.route) {
                MatchingScreen(
                    onStartChat = { partner ->
                    }
                )
            }

            composable(Screen.ChatList.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("💬 대화 목록 화면", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            composable(Screen.Corrections.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("✏️ AI 오답노트 화면", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            composable(Screen.Profile.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("👤 내 프로필 화면", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}