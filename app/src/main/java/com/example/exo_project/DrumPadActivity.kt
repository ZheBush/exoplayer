package com.example.exo_project

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exo_project.ui.theme.Green198
import com.example.exo_project.ui.theme.Grey231
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Red222
import com.example.exo_project.ui.theme.White
import com.example.exo_project.ui.theme.ZeroA

class DrumPadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        enableEdgeToEdge()
        val sound = R.raw.wiz_khalifa_snare_3
        setContent {
            Scaffold { innerPadding ->
                DrumPad(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun DrumPad(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Green82)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(ZeroA)
        ) {

        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(ZeroA)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .background(ZeroA)
            ) {

            }
            Box(
                modifier = Modifier
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(topStart = 5.dp))
                    .border(
                        width = 1.dp,
                        color = Green82,
                        shape = RoundedCornerShape(topStart = 5.dp)
                    )
                    .fillMaxSize()
                    .background(Grey231)
                    .padding(15.dp)
            ) {
                DrumPart(sound = R.raw.wiz_khalifa_snare_3)
            }
        }
    }
}

@Composable
fun DrumPart(
    sound: Int
) {
    val soundList = remember {
        mutableStateListOf(
            true, false, true, false, true, false, true, false, true, false,
            true, false, true, false, false, false, false, false, false, false,
            true, false, true, false, true, false, true, false, true, false
        )
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(soundList) { index, item ->
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(5.dp))
                    .border(width = 0.5.dp, color = Green82, shape = RoundedCornerShape(5.dp))
                    .width(20.dp)
                    .height(35.dp)
                    .background(if (soundList[index]) Green198 else Green82)
                    .selectable(
                        selected = soundList[index],
                        onClick = { soundList[index] = !soundList[index] }
                    )
            ) {}
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec: width=411dp, height=891dp, orientation=landscape"
)
@Composable
fun DrumPadPreview() {
    DrumPad()
}