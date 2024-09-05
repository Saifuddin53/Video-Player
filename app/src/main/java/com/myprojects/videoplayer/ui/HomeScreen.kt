package com.myprojects.videoplayer.ui

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.myprojects.videoplayer.VideoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun HomeScreen(
    modifier: Modifier,
) {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var outputPath by remember{ mutableStateOf<String?>(null) }

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
        if(videoUri == null) {
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
        } else {
            LaunchedEffect(key1 = true) {
                try {
                    val videoPath = getFilePathFromUri(context, videoUri!!)
                    outputPath = convertToHls(context, videoPath!!)
                }catch (e: Exception) {

                }
            }
        }
    }

    if(outputPath != null) {
        VideoPlayer(videoUrl = outputPath.toString())
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


suspend fun convertToHls(context: Context, srcPath: String): String? {
    return withContext(Dispatchers.IO) {

        val dirName = "hls"

        val outputDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            dirName
        )

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        } else {
            outputDir.listFiles()?.forEach {
                it.delete()
            }
        }
        val destFile = File(outputDir, "output.m3u8")


        val command =
            "-i $srcPath -codec: copy -start_number 0 -hls_time 10 -hls_list_size 0 -f hls $destFile"

        val session = FFmpegKit.execute(command)

        return@withContext if (ReturnCode.isSuccess(session.returnCode)) {
            Log.i("FFmpeg", "Converted successfully")
            destFile.absolutePath // Return the path of the output file
        } else {
            Log.e("FFmpeg", "Failed to convert video.")
            null
        }
    }
}


