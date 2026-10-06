package com.example.voiceassistant

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class CrashReportActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val report = getSharedPreferences("crash", MODE_PRIVATE)
            .getString("report", "No crash report found.")

        val text = TextView(this)

        text.text = report
        text.textSize = 14f
        text.setPadding(30, 60, 30, 30)

        setContentView(text)
    }
}
