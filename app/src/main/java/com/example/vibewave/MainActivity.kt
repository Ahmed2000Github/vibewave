package com.example.vibewave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.vibewave.presentation.navigation.AppNavHost
import com.example.vibewave.ui.theme.VibeWaveTheme
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 1001

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions() {
        val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (checkSelfPermission(requiredPermission) == PackageManager.PERMISSION_GRANTED) {
            proceedWithApp()
        } else {
            if (shouldShowRequestPermissionRationale(requiredPermission)) {
                showPermissionRationale(requiredPermission)
            } else {
                requestPermissions(arrayOf(requiredPermission), PERMISSION_REQUEST_CODE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionRationale(permission: String) {
        AlertDialog.Builder(this)
            .setTitle("Permission Needed")
            .setMessage("This app needs access to your music files to play songs.")
            .setPositiveButton("OK") { _, _ ->
                requestPermissions(arrayOf(permission), PERMISSION_REQUEST_CODE)
            }
            .setNegativeButton("Exit") { _, _ ->
                finishAffinity()
            }
            .setCancelable(false)
            .show()
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedWithApp()
            } else {
                if (shouldShowRequestPermissionRationale(permissions[0])) {
                    checkPermissions()
                } else {
                    openAppSettings()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun openAppSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            startActivity(this)
        }
        finishAffinity()
    }

    private fun proceedWithApp() {
        setContent {
            VibeWaveTheme(darkTheme = false) {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}