package com.muco.disneyapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.fragment.app.FragmentActivity
import com.muco.disneyapp.compose.DisneyApp
import com.muco.disneyapp.ui.theme.DisneyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisneyAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisneyApp()
                }
            }
        }
    }
}