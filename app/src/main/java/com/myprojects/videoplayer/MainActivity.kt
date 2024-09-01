package com.myprojects.videoplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myprojects.videoplayer.ui.HomeScreen
import com.myprojects.videoplayer.ui.theme.VideoPlayerTheme

class MainActivity : ComponentActivity() {

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // All permissions are granted
            Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
        } else {
            // At least one permission is denied
            Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoPlayerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        when {
            allPermissionsGranted() -> {
                // Permissions are already granted
                Toast.makeText(this, "Permissions already granted!", Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale() -> {
                // Show an explanation to the user
                Toast.makeText(this, "Permissions are required for this app.", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(permissions)
            }
            else -> {
                // Directly request for required permissions
                requestPermissionLauncher.launch(permissions)
            }
        }
    }

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale() = permissions.any {
        ActivityCompat.shouldShowRequestPermissionRationale(this, it)
    }
}
