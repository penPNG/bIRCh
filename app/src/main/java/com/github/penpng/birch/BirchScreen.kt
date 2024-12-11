package com.github.penpng.birch

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.datastore.core.DataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.penpng.birch.data.Connection
import com.github.penpng.birch.data.UserPreferencesRepository
import com.github.penpng.birch.ui.BirchViewModel
import com.github.penpng.birch.ui.ChatScreen
import com.github.penpng.birch.ui.ConnectScreen
import java.util.prefs.Preferences

enum class bIRChScreen(@StringRes val title: Int) {
    Login(title = R.string.app_name),
    Chat(title = R.string.chat_screen)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BirchApp(
    viewModel: BirchViewModel = viewModel(),
//    upr: UserPreferencesRepository,
//    nickname: String,
//    dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
    navController: NavHostController = rememberNavController()
) {

    Scaffold { //innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = bIRChScreen.Login.name,
            //modifier = Modifier.padding(innerPadding),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        ) {
           composable(route = bIRChScreen.Login.name) {
                ConnectScreen(
//                    nickname,
//                    upr = upr,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    onConnectButtonClicked = {
                        viewModel.createConnection(Connection(viewModel.getNick(), viewModel.getServer(), viewModel))
                        navController.navigate(bIRChScreen.Chat.name)
                    },
                    viewModel = viewModel,
//                    dataStore = dataStore
                )
           }
           composable(route = bIRChScreen.Chat.name) {
                ChatScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    onDisconnectButtonClicked = {
                        viewModel.closeConnection()
                        navController.popBackStack(bIRChScreen.Login.name, inclusive = false)
                    }
                )
           }
        }
    }
}