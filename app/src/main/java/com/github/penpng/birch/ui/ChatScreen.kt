package com.github.penpng.birch.ui

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import com.github.penpng.birch.R
import com.github.penpng.birch.data.BirchUIState
import com.github.penpng.birch.ui.theme.BirchTheme
import com.github.penpng.birch.ui.theme.Pink40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    navigateUp: () -> Unit,
    scope: CoroutineScope,
    viewModel: BirchViewModel,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        TopAppBar(
            title = { Text(viewModel.getServer()) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Pink40
            ),
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painter = painterResource(R.drawable.logout_ic),
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        painter = painterResource(R.drawable.users_ic),
                        contentDescription = null
                    )
                }
            }
        )
    }
}


@Composable
fun ChatScreen(
    viewModel: BirchViewModel,
    uiState: BirchUIState,
    modifier: Modifier = Modifier,
    onDisconnectButtonClicked: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items =
        listOf(
            "Bob",
            "Stphen",
            "Clair",
            "Matt",
            "Kathy",
            "Julia",
            "Jasmen",
            "Gabe",
            "Amanda",
            "Lexi",
            "Scarlett",
            "Tyler",
            "Tori",
            "Art",
            "Jazlyn",
            "Jeanette",
            "Sam",
            "David",
            "Wanda"
        )
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(items[0]) }
    var chat by remember { mutableStateOf("")}
    var text by remember { mutableStateOf("") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                //CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    ModalDrawerSheet {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            Spacer(Modifier.height(12.dp))
                            items.forEach { item ->
                                NavigationDrawerItem(
                                    icon = { Icon(painter = painterResource(R.drawable.user_ic), contentDescription = null) },
                                    label = { Text(item) },
                                    selected = false,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        text = item
                                        selectedItem = item
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    }
                //}
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            ChatAppBar(
                                navigateUp = onDisconnectButtonClicked,
                                scope = scope,
                                drawerState = drawerState,
                                viewModel = viewModel
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Surface(color = Color(0xFF201D1E)) {
                                var offset by remember { mutableStateOf(0f) }
                                Text(viewModel.getChat(),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                        .height(690.dp)
                                        .verticalScroll(
                                            rememberScrollState(),
                                            reverseScrolling = true
                                        ),
                                    textAlign = TextAlign.Left,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE97263)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                TextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    label = { Text("Message") },
                                    maxLines = 1,
                                    modifier = Modifier
                                        .weight(weight = 1f, fill = true)
                                        .onKeyEvent {
                                            if (it.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                                                if (text != "") {
                                                    viewModel.updateChat(viewModel.getNick() + ": " + text)
                                                    //viewModel.sendMessage(text)

                                                }
                                                //println(viewModel.uiState.value.chat.toString())
                                                text = ""
                                                true
                                            }
                                            false
                                        }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilledIconButton(
                                    onClick = {
                                        //chat=chat+viewModel.uiState.value.nickname+text+"\n"
                                        if (text != "") {
                                            viewModel.updateChat(viewModel.getNick()+": "+text)                                            //viewModel.sendMessage(text)
                                        }
                                        //println(viewModel.uiState.value.chat.toString())
                                        text = "" },
                                    modifier = Modifier.size(60.dp, 60.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.send_ic),
                                        contentDescription = null,
                                        modifier = modifier.scale(1.25f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChatScreenDemo() {
    BirchTheme(darkTheme = true,
        dynamicColor = false) {
        ChatScreen(
            viewModel = BirchViewModel(),
            uiState = BirchUIState(),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium)),
            onDisconnectButtonClicked = {}
        )
    }
}