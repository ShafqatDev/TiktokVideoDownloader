package com.example.tiktokvideodownloader.myapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tiktokvideodownloader.presentation.components.LocalNavController
import com.example.tiktokvideodownloader.presentation.navigation.Navigation
import com.example.tiktokvideodownloader.presentation.tiktok.TiktokScreen
import com.example.tiktokvideodownloader.ui.theme.TiktokVideoDownloaderTheme
import com.example.tiktokvideodownloader.util.Constants.permissionList
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TiktokVideoDownloaderTheme{
                CompositionLocalProvider(value = LocalNavController provides navController) {
                    val permission = rememberMultiplePermissionsState(permissions = permissionList)
                    if (permission.allPermissionsGranted){
                        Navigation(navController = navController)
                    }else{
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            IconButton(onClick = {
                                permission.launchMultiplePermissionRequest()
                            }) {
                                Text(text = "Allow Permissions")
                            }
                        }
                    }
                }
            }
        }
    }
}