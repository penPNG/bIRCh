package com.github.penpng.birch.data


data class BirchUIState (
    val nickname: String = "",
    val server: String = "",
    var chat: StringBuilder = StringBuilder(""),
    val users: List<String> = listOf()
)