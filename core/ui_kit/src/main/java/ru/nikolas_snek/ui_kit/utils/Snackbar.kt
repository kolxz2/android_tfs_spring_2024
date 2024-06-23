package ru.nikolas_snek.ui_kit.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbarWithAction(view: View, message: String, actionText: String, action: () -> Unit) {
	val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
	snackbar.setAction(actionText) {
		action()
	}
	snackbar.show()
}