package com.mco.mchat.utils.Ext

import java.text.SimpleDateFormat
import java.util.*

const val FORMAT_DATE_SIMPLE = "MMM dd, yyyy"
const val FORMAT_DATE_SIMPLE_TIME = "MMM dd, yyyy HH:ss"

fun Long.getDateTimeFromEpocLongOfSeconds(): String? {
    return try {
        val format = SimpleDateFormat(FORMAT_DATE_SIMPLE, Locale.getDefault())
        format.format(Date(this))
    } catch (e: Exception) {
        e.toString()
    }
}

fun Long.toDateTime(): String? {
    return try {
        val format = SimpleDateFormat(FORMAT_DATE_SIMPLE_TIME, Locale.getDefault())
        format.format(Date(this))
    } catch (e: Exception) {
        e.toString()
    }
}