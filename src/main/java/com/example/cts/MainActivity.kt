package com.example.cts

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        // If launched via shortcut or assist action, trigger the assistant session
        if (intent?.action == Intent.ACTION_ASSIST) {
            showAssist(null)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup UI to guide user to set as default assistant
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 50, 50, 50)
        }

        val textView = TextView(this).apply {
            text = "CTS: Search any screen\n\nTo use this app like 'Circle to Search' without permission prompts:\n\n1. Tap 'Set as Default Assistant' below.\n2. Tap 'Digital assistant app'.\n3. Select 'CTS'.\n4. Ensure 'Use screenshot' is enabled in settings.\n\nUsage:\nLong-press Home (or swipe from corner) to instantly search your current screen with Google Lens."
            gravity = Gravity.CENTER
            textSize = 18f
            setLineSpacing(0f, 1.2f)
        }

        val button = Button(this).apply {
            text = "Set as Default Assistant"
            setOnClickListener {
                val intent = Intent(Settings.ACTION_VOICE_INPUT_SETTINGS)
                startActivity(intent)
            }
        }

        layout.addView(textView)
        layout.addView(button)
        setContentView(layout)
    }
}
