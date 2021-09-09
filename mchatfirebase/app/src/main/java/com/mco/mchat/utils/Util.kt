package com.mco.mchat.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.mco.mchat.R

fun View.showSnackBar(text: String) {
    Snackbar.make(this.rootView.findViewById(R.id.main_layout), text, Snackbar.LENGTH_SHORT).show()
}
