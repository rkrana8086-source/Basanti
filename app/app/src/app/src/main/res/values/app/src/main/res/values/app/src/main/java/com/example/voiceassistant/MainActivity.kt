package com.example.voiceassistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var recognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        setContent {
            AssistantScreen()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("hi", "IN")
            tts?.setSpeechRate(0.95f)
        }
    }

    private fun speak(text: String) {
        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "assistant"
        )
    }

    private fun listen(
        onResult: (String) -> Unit,
        onState: (Boolean) -> Unit
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            speak("इस फोन पर voice recognition उपलब्ध नहीं है।")
            return
        }

        recognizer?.destroy()
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)

        recognizer?.setRecognitionListener(
            object : RecognitionListener {

                override fun onReadyForSpeech(params: Bundle?) {
                    onState(true)
                }

                override fun onBeginningOfSpeech() {
                    onState(true)
                }

                override fun onEndOfSpeech() {
                    onState(false)
                }

                override fun onError(error: Int) {
                    onState(false)
                }

                override fun onResults(results: Bundle?) {
                    onState(false)

                    val result =
                        results?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )?.firstOrNull()

                    if (!result.isNullOrBlank()) {
                        onResult(result)
                    }
                }

                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onPartialResults(
                    partialResults: Bundle?
                ) {}
                override fun onEvent(
                    eventType: Int,
                    params: Bundle?
                ) {}
            }
        )

        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "hi-IN"
            )
        }

        recognizer?.startListening(intent)
    }

    @Composable
    private fun AssistantScreen() {

        var listening by remember {
            mutableStateOf(false)
        }

        var message by remember {
            mutableStateOf(
                "नमस्ते! मैं आपका AI Assistant हूँ।"
            )
        }

        var hasMic by remember {
            mutableStateOf(
                checkSelfPermission(
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val permissionLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                hasMic = granted
            }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF090B12)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "AI ASSISTANT",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (listening)
                        "सुन रहा हूँ..."
                    else
                        "बोलिए",
                    color = Color(0xFFB9B5FF),
                    fontSize = 15.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(
                            RoundedCornerShape(30.dp)
                        )
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF292D46),
                                    Color(0xFF10121C)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "AI",
                            color = Color(0xFFE8E5FF),
                            fontSize = 76.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "आपकी Assistant",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                        .background(
                            Color(0xFF171B2A)
                        )
                        .padding(18.dp)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        if (!hasMic) {

                            permissionLauncher.launch(
                                Manifest.permission.RECORD_AUDIO
                            )

                        } else {

                            listen(
                                onResult = { heard ->

                                    val reply =
                                        "समझ गया। आपने कहा: $heard"

                                    message = reply
                                    speak(reply)
                                },
                                onState = {
                                    listening = it
                                }
                            )
                        }
                    },
                    modifier = Modifier.size(92.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF6556FF)
                    )
                ) {

                    Text(
                        text = "🎙",
                        fontSize = 30.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "माइक दबाकर बोलें",
                    color = Color(0xFF9DA0AF),
                    fontSize = 13.sp
                )
            }
        }
    }

    override fun onDestroy() {
        recognizer?.destroy()
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
