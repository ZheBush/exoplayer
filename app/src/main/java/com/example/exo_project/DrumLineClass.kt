package com.example.exo_project

data class DrumLineClass(
    val title: String,
    val resIndex: Int,
    val tactList: MutableList<Boolean>,
    var isSolo: Boolean
)
