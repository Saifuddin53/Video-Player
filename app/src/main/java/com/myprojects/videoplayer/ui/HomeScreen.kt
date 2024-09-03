package com.myprojects.videoplayer.ui

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Video
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.myprojects.videoplayer.VideoPlayer
import com.myprojects.videoplayer.VideoState
import java.io.File

@Composable
fun HomeScreen(
    modifier: Modifier
) {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var videoState by remember { mutableStateOf<VideoState>(VideoState.Idle()) }

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
        when(videoState) {
            is VideoState.Loading -> {
                Button(
                    onClick = {
//                        videoPicker.launch("video/*")
                    }
                ) {
                    Text(
                        text = "Play",
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            is VideoState.Success -> {

            }
            is VideoState.Error -> {

            }
            is VideoState.Idle -> {
                Button(
                    onClick = {
                        videoPicker.launch("video/*")
                    }
                ) {
                    Text(
                        text = videoUri.toString(),
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            is VideoState.Converting -> {
                CircularProgressIndicator()

                val customDir = File(context.filesDir, "sideo")  // This creates a folder named "Android" in internal storage
                if (!customDir.exists()) {
                    Toast.makeText(context, "Directory doesn't exist!", Toast.LENGTH_SHORT).show()
                    customDir.mkdir()  // Create the directory if it doesn't exist
                }
                val outputPath = File(customDir, "output_video.m3u8").absolutePath  // Complete output path

                val inputPath = getFilePathFromUri(context, videoUri!!)

                // FFMPEG command
                val command = arrayOf(
                    "-i", inputPath,  // The input file path (MP4 video)
                    "-codec", "copy",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-hls_segment_filename", "${customDir}/segment_%03d.ts",  // The pattern for segment files
                    outputPath  // The output M3U8 file path
                )

                // Execute the command using FFmpeg
                FFmpeg.executeAsync(command) { executionId, returnCode ->
                    if (returnCode == RETURN_CODE_SUCCESS) {
//                        Log.d("FFmpeg", "Conversion success! Output Path: $outputPath")
                        videoState = VideoState.Loading(outputPath)
                    } else {
                        Log.d("FFmpeg", "Conversion failed!")
                    }
                }
            }
            else -> {}
        }
        if(videoUri != null) {
            videoState = VideoState.Converting()
        }
    }
}

fun getFilePathFromUri(context: Context, uri: Uri): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.let {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            filePath = it.getString(columnIndex)
        }
        it.close()
    }
    return filePath
}
