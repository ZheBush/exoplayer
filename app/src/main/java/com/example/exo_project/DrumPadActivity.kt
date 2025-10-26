package com.example.exo_project

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.exo_project.ui.theme.Black
import com.example.exo_project.ui.theme.Green198
import com.example.exo_project.ui.theme.Green52
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Grey168
import com.example.exo_project.ui.theme.Grey224
import com.example.exo_project.ui.theme.Red222
import com.example.exo_project.waveform.Waveform
import com.example.exo_project.waveform.parseWavData
import com.example.exo_project.waveform.readAudioData
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
                DrumPadPreview()
//                DrumPad(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun DrumPad(
    modifier: Modifier = Modifier
) {
    val tactCount = 30
    val temp = 140F
    val timeSignature = 4
    var isSoundPlay by remember { mutableStateOf(false) }
    var isAddLineWindowOpen by remember { mutableStateOf(true) }
    val soundList = remember { mutableStateListOf<DrumLineClass>() }
    if (isAddLineWindowOpen) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { isAddLineWindowOpen = false }
        ) {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .width(600.dp)
                    .background(
                        color = Grey224,
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {

            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Green82)
        ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .background(Green82)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) { }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn {
                    itemsIndexed(soundList) { index, item ->
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 5.dp
                        )
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(
                            color = Green52,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontWeight = FontWeight(300),
                        fontSize = 16.sp,
                        color = Grey224
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Green82)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Green82)
            ) {
                IconToggleButton(
                    checked = isSoundPlay,
                    onCheckedChange = { isSoundPlay = it }
                ) {
                    Icon(
                        imageVector = if (!isSoundPlay) Icons.Filled.PlayArrow else Icons.Filled.Close,
                        contentDescription = "play",
                        tint = if (isSoundPlay) Red222 else Grey224
                    )
                }
            }
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(topStart = 5.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Green82,
                        shape = RoundedCornerShape(topStart = 5.dp)
                    )
                    .fillMaxSize()
                    .background(Grey224)
                    .padding(top = 5.dp)
                    .clickable(onClick = { isAddLineWindowOpen = true })
            ) {
                LazyColumn {
                    itemsIndexed(soundList) { index, item ->
                        DrumLine(
                            item = item,
                            tactCount = tactCount,
                            isSoundPlay = isSoundPlay,
                            temp = temp,
                            timeSignature = timeSignature
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrumLine(
    item: DrumLineClass,
    tactCount: Int,
    isSoundPlay: Boolean,
    temp: Float,
    timeSignature: Int,
) {
    var count by remember { mutableIntStateOf(0) }
    val tactDuration = (60 / temp / timeSignature * 1000).toLong()
    LaunchedEffect(isSoundPlay) {
        while (isSoundPlay && count < tactCount) {
            delay(tactDuration)
            count++
            if (count == tactCount) {
                count = 0
            }
        }
    }
    val lazyState = rememberLazyListState()
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val exoPlayer = remember {
        if (isPreview) {
            null
        }
        else {
            ExoPlayer.Builder(context).build().apply {
                setAudioAttributes(
                    androidx.media3.common.AudioAttributes.Builder()
                        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .setUsage(C.USAGE_MEDIA)
                        .build(),
                    true
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        val rawUri = "android.resource://${context.packageName}/${item.resIndex}"
        val mediaItem = MediaItem.fromUri(rawUri)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    start = 15.dp
                )
                .selectable(
                    selected = item.isSolo,
                    onClick = { item.isSolo = !item.isSolo }
                ),
            text = "S",
            textDecoration = if (item.isSolo) TextDecoration.Underline else TextDecoration.None,
            fontSize = 18.sp,
            color = if (item.isSolo) Green198 else Green82)
        LazyRow(
            state = lazyState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            itemsIndexed(item.tactList) { index, i ->
                Box(
                    modifier = Modifier
                        .padding(start = if (index == 0) 10.dp else 0.dp)
                        .fillMaxHeight()
                        .width(25.dp)
                        .background(
                            if (isSoundPlay && index == count) Grey168 else Grey224,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 3.dp)
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .border(
                                width = 0.5.dp,
                                color = Green82,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .width(20.dp)
                            .height(35.dp)
                            .background(
                                if (item.tactList[index]) Green198 else {
                                    if (index % 8 < 4) Green82
                                    else Green52
                                }
                            )
                            .selectable(
                                selected = i,
                                onClick = { item.tactList[index] = !item.tactList[index] })
                    )
                }
            }
        }
    }
}

fun getRawFiles(context: Context): List<RawFile> {
    return try {
        val resources = context.resources
        val packageName = context.packageName
        val field = R.raw::class.java.fields
        field.map { field ->
            val id = field.getInt(R.raw::class.java)
            val name = resources.getResourceEntryName(id)
            RawFile(
                id = id,
                name = name
            )
        }
    }
    catch (e: Exception) {
        emptyList()
    }
}

@Preview(
    showBackground = true,
    device = "spec: width=411dp, height=891dp, orientation=landscape"
)
@Composable
fun DrumPadPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .width(600.dp)
                .background(
                    color = Grey224,
                    shape = RoundedCornerShape(5.dp)
                ),
            verticalArrangement = Arrangement.SpaceEvenly
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
                    .height(210.dp)
                    .fillMaxWidth()
            ) {
                var expanded by remember { mutableStateOf(false) }
                var selectedFile by remember { mutableStateOf<RawFile?>(null) }
                val context = LocalContext.current
                val rawList = remember { getRawFiles(context) }
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
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxSize()
                    ) {
                        var isPlaying by remember { mutableStateOf(false) }
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
                                onClick = { isPlaying = !isPlaying }
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
                        var colorProgress by remember { mutableFloatStateOf(0.1f) }
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
                        LaunchedEffect(isPlaying) {
                            while (isPlaying && progress < 1f && colorProgress > 0f) {
                                delay(16)
                                progress += 0.05f
                                if (progress >= 1f) {
                                    while (colorProgress > 0f) {
                                        delay(16)
                                        colorProgress -= 0.01f
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
                        Waveform(
                            amplitudes = waveformData.amplitudes,
                            isPlaying = isPlaying,
                            progress = animatedProgress,
                            colorProgress = animatedColorProgress
                        )
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
                    .fillMaxSize(),
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
//    DrumPad()
}