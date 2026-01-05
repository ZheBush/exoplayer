package com.example.exo_project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.exo_project.ui.theme.Green52
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Grey168
import com.example.exo_project.ui.theme.Grey206

@Composable
fun EqualizerWindow(
    sliderPositionList: List<MutableFloatState>
) {
    Row(
        modifier = Modifier
            .width(600.dp)
            .height(200.dp)
            .background(
                color = Grey206,
                shape = RoundedCornerShape(5.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        sliderPositionList.forEach { sliderPosition ->
            Slider(
                value = sliderPosition.floatValue,
                onValueChange = { sliderPosition.floatValue = it },
                colors = SliderDefaults.colors(
                    inactiveTrackColor = Grey168,
                    activeTrackColor = Green52,
                    thumbColor = Green82,
                ),
                modifier = Modifier
                    .width(150.dp)
                    .rotate(270f)
                    .offset(x = (-75).dp)
            )
        }
    }
}