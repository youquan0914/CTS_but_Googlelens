package com.example.cts

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AssistantSession(context: Context) : VoiceInteractionSession(context) {

    override fun onCreate() {
        super.onCreate()
        // Set an empty view so the session is considered "active"
        setContentView(android.view.View(context))
    }

    override fun onShow(args: Bundle?, showFlags: Int) {
        super.onShow(args, showFlags)
    }

    override fun onHandleScreenshot(screenshot: Bitmap?) {
        super.onHandleScreenshot(screenshot)
        if (screenshot != null) {
            saveAndShare(screenshot)
        }
        finish()
    }

    private fun startIntent(intent: Intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startAssistantActivity(intent)
        } else {
            context.startActivity(intent)
        }
    }

    private fun saveAndShare(bitmap: Bitmap) {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "screenshot.png")
        try {
            FileOutputStream(file).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        
        // Try multiple potential packages for Google Lens
        val packages = listOf("com.google.android.googlequicksearchbox", "com.google.ar.lens")
        var success = false
        
        for (pkg in packages) {
            val lensIntent = Intent("com.google.lens.intent.action.LENS_INPUT").apply {
                setData(contentUri)
                setPackage(pkg)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            try {
                // Explicitly grant permission for more stability
                context.grantUriPermission(pkg, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startIntent(lensIntent)
                success = true
                break
            } catch (e: Exception) {
                // Continue to next package or fallback
            }
        }

        if (!success) {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            
            try {
                sendIntent.setPackage("com.google.android.googlequicksearchbox")
                context.grantUriPermission("com.google.android.googlequicksearchbox", contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startIntent(sendIntent)
            } catch (e: Exception) {
                try {
                    sendIntent.setPackage(null)
                    val shareIntent = Intent.createChooser(sendIntent, "Search with...")
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startIntent(shareIntent)
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }
        
        // Free bitmap memory
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}
