package com.mco.mchat.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.mco.mchat.R
import com.mikepenz.fastadapter.binding.BindingViewHolder

fun View.showSnackBar(text: String) {
    Snackbar.make(this.rootView.findViewById(R.id.main_layout), text, Snackbar.LENGTH_SHORT).show()
}

inline fun <reified T : ViewBinding> RecyclerView.ViewHolder.asBinding(block: (T) -> View): View? {
    return if (this is BindingViewHolder<*> && this.binding is T) {
        block(this.binding as T)
    } else {
        null
    }
}

fun View.forceHideKeyboard() {
    val inputManager: InputMethodManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}
