package com.example.voiceassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->

            val error = buildString {
                appendLine("BASANTI CRASH REPORT")
                appendLine()
                appendLine("Exception:")
                appendLine(throwable.javaClass.name)
                appendLine()
                appendLine("Message:")
                appendLine(throwable.message ?: "No message")
                appendLine()
                appendLine("Cause:")
                appendLine(throwable.cause?.toString() ?: "None")
                appendLine()
                appendLine("Stack trace:")
                appendLine(
                    throwable.stackTraceToString()
                )
            }

            getSharedPreferences("crash", MODE_PRIVATE)
                .edit()
                .putString("last_crash", error)
                .apply()

            android.os.Process.killProcess(
                android.os.Process.myPid()
            )
        }

        setContent {
            DiagnosticScreen()
        }
    }

    @Composable
    private fun DiagnosticScreen() {

        val prefs = getSharedPreferences(
            "crash",
            MODE_PRIVATE
        )

        var crash by remember {
            mutableStateOf(
                prefs.getString("last_crash", null)
            )
        }

        MaterialTheme {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                Text(
                    text = "Basanti Diagnostic",
                    fontSize = 26.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                if (crash == null) {

                    Text(
                        text = """
                        App successfully started.

                        Ab app ko normally use karo.

                        Agar app crash hoti hai,
                        dobara Basanti kholo.

                        Crash report yahin dikhegi.
                        """.trimIndent(),
                        fontSize = 17.sp
                    )

                } else {

                    Text(
                        text = "CRASH FOUND",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = crash!!,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Button(
                        onClick = {
                            prefs.edit()
                                .remove("last_crash")
                                .apply()

                            crash = null
                        }
                    ) {
                        Text("Clear report")
                    }
                }
            }
        }
    }
}
