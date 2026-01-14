package com.jesse.sickstech.core.util

import androidx.core.view.isVisible
import com.jesse.sickstech.databinding.ViewToolbarBinding

fun ViewToolbarBinding.setupToolbar(
    title: String,
    showKeyboard: Boolean,
    onBack: () -> Unit,
    onKeyboardClick: (() -> Unit)? = null
) {
    toolbar.title = title
    imageViewKeyboard.isVisible = showKeyboard

    toolbar.setNavigationOnClickListener {
        onBack()
    }
}