package com.example.exo_project.classes

data class DrumLineClass(
    val title: String,
    val resIndex: Int,
    val tactList: MutableList<Boolean>,
    var isSolo: Boolean
)
