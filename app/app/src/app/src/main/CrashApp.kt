package com.example.voiceassistant

import android.app.Application
import android.content.Intent

class CrashApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->

            val report = """
                BASANTI CRASH REPORT

                Thread:
                ${thread.name}

                Exception:
                ${throwable.javaClass.name}

                Message:
                ${throwable.message}

                Stack:
                ${throwable.stackTraceToString()}
            """.trimIndent()

            getSharedPreferences("crash", MODE_PRIVATE)
                .edit()
                .putString("report", report)
                .apply()

            oldHandler?.uncaughtException(thread, throwable)
        }
    }
}
