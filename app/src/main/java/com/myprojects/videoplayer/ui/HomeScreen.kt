package com.myprojects.videoplayer.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myprojects.videoplayer.VideoPlayer

@Composable
fun HomeScreen(
    modifier: Modifier
) {
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                videoUri = uri
            }
        }
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(videoUri != null) {
            VideoPlayer(videoUrl = videoUri.toString())
            IconButton(
                onClick = { videoUri = null },
                modifier = Modifier.align(Alignment.TopEnd)
                ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        } else {
            Button(
                onClick = {
                    videoPicker.launch("video/*")
                }
            ) {
                Text(
                    text = "Upload video",
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
