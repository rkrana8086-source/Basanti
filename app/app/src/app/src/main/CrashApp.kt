package com.example.voiceassistant

import android.app.Application
import android.content.Intent

class CrashApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->

            val report = """
                BASANTI CRASH REPORT

                Exception:
                ${throwable.javaClass.name}

                Message:
                ${throwable.message ?: "No message"}

                Stack trace:
                ${throwable.stackTraceToString()}
            """.trimIndent()

            getSharedPreferences("crash", MODE_PRIVATE)
                .edit()
                .putString("report", report)
                .apply()

            val intent = Intent(this, CrashReportActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            startActivity(intent)
        }
    }
}
