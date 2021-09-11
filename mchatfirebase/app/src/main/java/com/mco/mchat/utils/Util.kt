package com.mco.mchat.utils

import android.view.View
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
