package dev.ebnbin.ebui

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("android:src")
fun setImageResource(view: ImageView, @DrawableRes drawableId: Int) {
    if (drawableId == 0) {
        view.setImageDrawable(null)
    } else {
        view.setImageResource(drawableId)
    }
}
