package com.example.exo_project

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.privacysandbox.tools.core.model.Type
import com.example.exo_project.ui.theme.Green198
import com.example.exo_project.ui.theme.Green52
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Grey231
import com.example.exo_project.ui.theme.Red222
import com.example.exo_project.ui.theme.ZeroA
import kotlinx.coroutines.delay

@Suppress("DEPRECATION")
class DrumPadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
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
    val tactCount = 30
    var isSoundPlay by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Green82)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(ZeroA)
        ) {
            Row(
                modifier = Modifier
                    .width(146.dp)
                    .background(ZeroA)
            ) {
                
            }
            IconToggleButton(
                checked = isSoundPlay,
                onCheckedChange = { isSoundPlay = it }
            ) {
                Icon(
                    imageVector = if (!isSoundPlay) Icons.Filled.PlayArrow else Icons.Filled.Close,
                    contentDescription = "play",
                    tint = if (isSoundPlay) Red222 else Grey231
                )
            }
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
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(topStart = 5.dp))
                    .border(
                        width = 1.dp,
                        color = Green82,
                        shape = RoundedCornerShape(topStart = 5.dp))
                    .fillMaxSize()
                    .background(Grey231)
                    .padding(top = 5.dp)
            ) {
                DrumLine(sound = R.raw.wiz_khalifa_snare_3, 30, isSoundPlay, 60F, 4)
            }
        }
    }
}

@Composable
fun DrumLine(
    sound: Int,
    tactCount: Int,
    isSoundPlay: Boolean,
    temp: Float,
    timeSignature: Int
) {
    var count by remember { mutableIntStateOf(0) }
    val tactDuration = (60 / temp / timeSignature * 1000).toLong()
    LaunchedEffect(isSoundPlay) {
        while (isSoundPlay && count < tactCount) {
            delay(tactDuration)
            count++
        }
    }
    val soundList = remember {
        mutableStateListOf(
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false
        )
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        itemsIndexed(soundList) { index, item ->
            Box(
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        start = if (index == 0) 10.dp else 5.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(5.dp))
                    .border(
                        width = 0.5.dp,
                        color = Green82,
                        shape = RoundedCornerShape(5.dp))
                    .width(20.dp)
                    .height(35.dp)
                    .background(
                        if (soundList[index]) Green198 else {
                            if (index % 8 < 4) Green82
                            else Green52 })
                    .selectable(
                        selected = soundList[index],
                        onClick = { soundList[index] = !soundList[index] })
            )
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