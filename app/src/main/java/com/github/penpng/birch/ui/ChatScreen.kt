package com.github.penpng.birch.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.penpng.birch.R
import com.github.penpng.birch.ui.theme.BirchTheme
import com.github.penpng.birch.ui.theme.Pink40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        TopAppBar(
            title = { Text("Server X") },
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
            }
        )
    }
}


@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onDisconnectButtonClicked: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items =
        listOf(
            Icons.Default.AccountCircle,
            Icons.Default.Email,
            Icons.Default.Favorite
        )
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(items[0]) }

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
                                    icon = { Icon(item, contentDescription = null) },
                                    label = { Text(item.name.substringAfterLast(".")) },
                                    selected = item == selectedItem,
                                    onClick = {
                                        scope.launch { drawerState.close() }
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
                                navigateUp = {}
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Surface(color = Color(0xFF201D1E)) {
                                var offset by remember { mutableStateOf(0f) }
                                Text("Text",
                                    modifier = Modifier.padding(8.dp).fillMaxWidth().height(690.dp)
                                        .scrollable(
                                            orientation = Orientation.Vertical,
                                            state = rememberScrollableState { delta ->
                                                offset += delta
                                                delta
                                            }
                                        ),
                                    textAlign = TextAlign.Left,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE97263)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                var text by remember { mutableStateOf("") }
                                TextField(
                                    value = text,
                                    onValueChange = { text = it },
                                    label = { Text("Message") },
                                    maxLines = 1,
                                    modifier = Modifier.weight(weight = 1f, fill = true)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilledIconButton(
                                    onClick = {},
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
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium)),
            onDisconnectButtonClicked = {}
        )
    }
}