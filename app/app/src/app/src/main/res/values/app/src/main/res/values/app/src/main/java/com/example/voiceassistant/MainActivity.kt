package com.example.voiceassistant

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = TextView(this)

        text.text = """
            BASANTI DIAGNOSTIC

            Startup OK

            Activity: android.app.Activity
            Compose: NOT USED
            AndroidX: NOT USED
        """.trimIndent()

        text.textSize = 22f
        text.setPadding(40, 80, 40, 40)

        setContentView(text)
    }
}
