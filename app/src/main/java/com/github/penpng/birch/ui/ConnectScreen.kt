package com.github.penpng.birch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.github.penpng.birch.data.PreferencesKeys
import com.github.penpng.birch.R
import com.github.penpng.birch.data.UserPreferences
import com.github.penpng.birch.data.UserPreferencesRepository
import com.github.penpng.birch.ui.theme.BirchTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun ConnectScreen(
//    nickname: String,
//    upr: UserPreferencesRepository,
    modifier: Modifier = Modifier,
    viewModel: BirchViewModel,
//    dataStore: DataStore<Preferences>,
    onConnectButtonClicked: () -> Unit
) {

    var currentNickname by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("irc.libera.chat", "irc.librairc.net")
    var selectedIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Image(
                painter = painterResource(R.drawable.birch1),
                contentDescription = null,
                modifier = Modifier.width(250.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            TextField(
                value = currentNickname,
                onValueChange = { currentNickname = it },
                label = { Text("Nickname") },
                maxLines = 1,
                modifier = Modifier.onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                        viewModel.updateServer(items[selectedIndex])
                        viewModel.updateNick(currentNickname)
                        onConnectButtonClicked()
                        true
                    }
                    false
                }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Box {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    ) {
                        Text(text = items[selectedIndex])
                        Image(
                            painter = painterResource(id = R.drawable.drop_down_ic_dark),
                            contentDescription = "Dropdown Icon"
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = {
//                        scope.launch {
//                            upr.updateNickname(currentNickname)
//                        }
                        viewModel.updateServer(items[selectedIndex])
                        viewModel.updateNick(currentNickname)
                        onConnectButtonClicked()
                    }) {
                        Text("Connect")
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }) {
                    items.forEachIndexed { index, username ->
                        DropdownMenuItem(text = {
                            Text(text = username)
                        },
                            onClick = {
                                expanded = false
                                selectedIndex = index
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
    }
}


@Preview
@Composable
fun ConnectPreview() {
    BirchTheme {
        ConnectScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium)),
            onConnectButtonClicked = {},
            viewModel = BirchViewModel()
        )
    }
}

