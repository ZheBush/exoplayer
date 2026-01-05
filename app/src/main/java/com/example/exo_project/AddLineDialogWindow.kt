package com.example.exo_project

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.exo_project.classes.RawFileClass
import com.example.exo_project.ui.theme.Green198
import com.example.exo_project.ui.theme.Green52
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Grey168
import com.example.exo_project.ui.theme.Grey224
import com.example.exo_project.waveform.Waveform
import com.example.exo_project.waveform.createExoPlayer
import com.example.exo_project.waveform.getWavDuration
import com.example.exo_project.waveform.parseWavData
import com.example.exo_project.waveform.playSound
import com.example.exo_project.waveform.readAudioData
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLineDialogWindow(
    isAddLineWindowOpen: MutableState<Boolean>
) {
    if (isAddLineWindowOpen.value) {
        BasicAlertDialog(
            onDismissRequest = { isAddLineWindowOpen.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedFile by remember { mutableStateOf<RawFileClass?>(null) }
            val dynamicHeight = remember { derivedStateOf { if (selectedFile == null) 310.dp else 500.dp } }
            var sliderPosition by remember{ mutableFloatStateOf(0f) }
            val context = LocalContext.current
            val rawList = remember { getRawFiles(context) }
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .heightIn(max = 310.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(dynamicHeight.value)
                        .width(600.dp)
                        .background(
                            color = Grey224,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 16.dp
                            ),
                            text = "Add Sound",
                            fontSize = 18.sp,
                            fontWeight = FontWeight(350),
                            color = Green82
                        )
                        Column(
                            modifier = Modifier
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                )
                                .fillMaxSize()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedFile != null) "The ${selectedFile!!.name} is currently selected"
                                    else "There is no file here right now. You can add a new one",
                                    fontWeight = FontWeight(300)
                                )
                                IconButton(
                                    modifier = Modifier
                                        .padding(start = 2.dp)
                                        .size(22.dp),
                                    onClick = { expanded = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowDown,
                                        contentDescription = "choose file",
                                        tint = Green82
                                    )
                                }
                            }
                            DropdownMenu(
                                modifier = Modifier.heightIn(max = 200.dp),
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(
                                    x = 295.dp,
                                    y = 125.dp
                                )
                            ) {
                                if (rawList.isEmpty()) {
                                    DropdownMenuItem(
                                        onClick = { expanded = false },
                                        text = {
                                            Text("There are no files here")
                                        }
                                    )
                                }
                                else {
                                    rawList.forEach { file ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded = false
                                                selectedFile = file
                                            },
                                            text = {
                                                Text(file.name)
                                            }
                                        )
                                    }
                                }
                            }
                            if (selectedFile != null) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .fillMaxWidth()
                                            .height(100.dp)
                                    ) {

                                        var isPlaying by remember { mutableStateOf(false) }
                                        var duration by remember { mutableStateOf<Long?>(0L) }
                                        val exoPlayer = remember { createExoPlayer(context) }

                                        DisposableEffect(Unit) {
                                            onDispose {
                                                exoPlayer.release()
                                            }
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "You can prelisten the sound",
                                                fontWeight = FontWeight(300)
                                            )
                                            IconButton(
                                                modifier = Modifier
                                                    .padding(start = 2.dp)
                                                    .size(20.dp),
                                                onClick = {
                                                    isPlaying = !isPlaying
                                                    playSound(exoPlayer, context, selectedFile!!.id)
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (!isPlaying) Icons.Filled.PlayArrow else Icons.Filled.Close,
                                                    contentDescription = "prelisten sound",
                                                    tint = Green82
                                                )
                                            }
                                        }

                                        val inputStream = context.resources.openRawResource(selectedFile!!.id)
                                        val wavBytes = readAudioData(inputStream)
                                        val waveformData = remember(wavBytes) {
                                            parseWavData(
                                                wavBytes = wavBytes,
                                                channels = 2,
                                                samples = 2000
                                            )
                                        }

                                        var progress by remember { mutableFloatStateOf(0f) }
                                        var colorProgress by remember { mutableFloatStateOf(0.11f) }
                                        val animatedProgress by animateFloatAsState(
                                            targetValue = progress,
                                            animationSpec = tween(durationMillis = 16),
                                            label = "progressAnimation"
                                        )
                                        val animatedColorProgress by animateFloatAsState(
                                            targetValue = colorProgress,
                                            animationSpec = tween(durationMillis = 16),
                                            label = "progressColor"
                                        )

                                        LaunchedEffect(selectedFile!!.id) {
                                            duration = getWavDuration(context, selectedFile!!.id)
                                        }

                                        LaunchedEffect(selectedFile?.id) {
                                            progress = 0f
                                            colorProgress = 0.1f
                                            isPlaying = false
                                        }

                                        LaunchedEffect(isPlaying) {
                                            while (isPlaying && progress < 1f && colorProgress > 0f) {
                                                delay(16)
                                                progress += 1f / duration!! * 16
                                                if (progress >= 1f) {
                                                    while (colorProgress > 0f) {
                                                        delay(16)
                                                        colorProgress -= 0.008f
                                                        if (colorProgress <= 0f) {
                                                            break
                                                        }
                                                    }
                                                }
                                            }
                                            if (progress >= 1f) {
                                                progress = 0f
                                            }
                                            colorProgress = 0.1f
                                            isPlaying = false
                                        }
                                        Box(
                                            modifier = Modifier
                                                .heightIn(120.dp)
                                                .fillMaxWidth()
                                                .background(Grey168),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Waveform(
                                                amplitudes = waveformData.amplitudes,
                                                isPlaying = isPlaying,
                                                progress = animatedProgress,
                                                color = Green82,
                                                colorProgress = animatedColorProgress
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(40.dp))
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(200.dp)
                                            .rotate(270f),
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Slider(
                                            value = sliderPosition,
                                            onValueChange = { sliderPosition = it },
                                            colors = SliderDefaults.colors(
                                                inactiveTrackColor = Grey168,
                                                activeTrackColor = Green52,
                                                thumbColor = Green82,
                                            ),
                                            modifier = Modifier.width(150.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                            .height(30.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "add",
                                tint = Green82
                            )
                        }
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "add",
                                tint = Green82
                            )
                        }
                    }
                }
            }
        }
    }
}