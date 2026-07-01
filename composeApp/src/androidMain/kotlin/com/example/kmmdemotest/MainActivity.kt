package com.example.kmmdemotest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }

        // loopLog()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("onuszhao", "onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d("onuszhao", "onTrimMemory level=$level")
    }

    companion object {
        // val largeByteArray = ByteArray(500 * 1024 * 1024)

        // private var hasLoop = false
        // fun loopLog() {
        //     if (hasLoop) return
        //     hasLoop = true
        //     GlobalScope.launch {
        //         var index = 0
        //         while (true) {
        //             Log.d("onuszhao", "onuszhao  loop index=$index")
        //             index++
        //             delay(1000)
        //         }
        //     }
        // }
    }
}