package com.mco.mchat.utils.Ext

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import java.io.ByteArrayOutputStream


fun Context.openAppSettings() {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    })
}

fun Context.loadImageFromStorage(uri: Uri): ByteArray?{
    return try {
        val ips = contentResolver.openInputStream(uri)
        ByteArrayOutputStream().let{
            BitmapFactory.decodeStream(ips).compress(Bitmap.CompressFormat.PNG, 90, it)
            it.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}