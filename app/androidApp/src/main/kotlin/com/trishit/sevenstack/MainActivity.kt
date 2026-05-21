package com.trishit.sevenstack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.trishit.sevenstack.di.appModule
import com.trishit.sevenstack.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinContext
import org.koin.dsl.koinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val context = LocalContext.current
    val koin = remember {
        koinApplication {
            androidContext(context)
            modules(appModule, platformModule)
        }.koin
    }
    KoinContext(koin = koin) {
        App()
    }
}
