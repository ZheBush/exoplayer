package com.example.exo_project.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.exo_project.DrumPad

val ZeroA = Color(0, 0, 0, 0)

val White = Color(255, 255, 255, 255)
val Grey224 = Color(224, 224, 224, 255)
val Grey206 = Color(206, 206, 206, 255)
val Grey168 = Color(168, 168, 168, 255)

val Green198 = Color(0, 198, 142, 255)
val Green82 = Color(0, 82, 58, 255)
val Green52 = Color(0, 52, 35, 255)

val Red222 = Color(222, 42, 101, 255)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@Preview(
    showBackground = true,
    device = "spec: width=411dp, height=891dp, orientation=landscape"
)
@Composable
fun DrumPadPreview() {
    DrumPad()
}