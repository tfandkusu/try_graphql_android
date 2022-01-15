package com.tfandkusu.graphql.view.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tfandkusu.graphql.compose.edit.EditScreen
import com.tfandkusu.graphql.compose.home.HomeScreen
import com.tfandkusu.graphql.viewmodel.edit.EditViewModelImpl
import com.tfandkusu.graphql.viewmodel.home.HomeViewModelImpl

@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel = hiltViewModel<HomeViewModelImpl>()
            HomeScreen(viewModel) { number ->
                navController.navigate("edit/$number")
            }
        }
        composable(
            "edit/{number}",
            arguments = listOf(
                navArgument("number") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val viewModel = hiltViewModel<EditViewModelImpl>()
            EditScreen(viewModel, backStackEntry.arguments?.getInt("number") ?: 0)
        }
    }
}
