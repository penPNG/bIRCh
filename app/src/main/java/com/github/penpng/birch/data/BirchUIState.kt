package com.github.penpng.birch.data

import android.os.Handler
import android.os.Looper


data class BirchUIState(
    val nickname: String = "",
    val server: String = "",
    var chat: StringBuilder = StringBuilder(""),
    val users: List<String> = listOf(),
    val mainHandler: Handler = Handler(Looper.getMainLooper()),
    val connection: Connection? = null
)