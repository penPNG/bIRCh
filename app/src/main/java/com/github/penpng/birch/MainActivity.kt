package com.github.penpng.birch

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import com.github.penpng.birch.data.UserPreferences
import com.github.penpng.birch.data.UserPreferencesRepository
import com.github.penpng.birch.ui.theme.BirchTheme
import kotlinx.coroutines.flow.combine
import java.net.Socket

class MainActivity : ComponentActivity() {
//    val Context.dataStore by preferencesDataStore(
//        name = "user_data"
//    )
//    private var nickname: String = ""
//    private val userPreferencesRepository = UserPreferencesRepository(dataStore)
//    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
//    private val connectFlow = combine(userPreferencesRepository.userPreferencesFlow, userPreferencesFlow) {
//            userPreferencesJunk: UserPreferences, userPreferences: UserPreferences ->
//        nickname = userPreferences.nickname.toString()
//    }

    // Saving data on android is dumb
    // I won't be doing it
    // I will suffer the grade for it
    // I'm not sorry
    // In the meantime, internet functionality should be a breeze lmao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirchTheme(darkTheme = true,
                dynamicColor = false) {
                BirchApp()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BirchTheme {
        Greeting("Android")
    }
}