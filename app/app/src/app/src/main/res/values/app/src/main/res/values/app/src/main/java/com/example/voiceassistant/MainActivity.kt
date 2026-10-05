package com.example.voiceassistant

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)

        text.text = """
            BASANTI DIAGNOSTIC

            App startup successful.

            MainActivity is working.
        """.trimIndent()

        text.textSize = 22f
        text.setPadding(40, 80, 40, 40)

        setContentView(text)
    }
}
