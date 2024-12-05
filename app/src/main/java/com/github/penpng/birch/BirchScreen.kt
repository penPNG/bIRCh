package com.github.penpng.birch

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.penpng.birch.ui.ChatScreen
import com.github.penpng.birch.ui.ConnectScreen
import com.github.penpng.birch.ui.TempViewModel

enum class bIRChScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    Chat(title = R.string.chat_screen)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BirchApp(
    viewModel: TempViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    Scaffold { //innerPadding ->

        NavHost(
            navController = navController,
            startDestination = bIRChScreen.Login.name,
            //modifier = Modifier.padding(innerPadding),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        ) {
           composable(route = bIRChScreen.Login.name) {
                ConnectScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    onConnectButtonClicked = {
                        navController.navigate(bIRChScreen.Chat.name)
                    }
                )
           }
           composable(route = bIRChScreen.Chat.name) {
                ChatScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    onDisconnectButtonClicked = {
                        navController.navigate(bIRChScreen.Login.name)
                    }
                )
           }
        }
    }
}