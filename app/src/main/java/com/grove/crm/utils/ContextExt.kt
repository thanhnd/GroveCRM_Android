package com.grove.crm.utils

import android.app.AlertDialog
import android.content.Context
import android.text.Spanned
import com.grove.crm.R

fun Context.showAlertDialog(title: String? = null, message: Spanned? = null, onClose: (() -> Unit)? = null) {
    AlertDialog.Builder(this).apply {
        setTitle(title ?: getString(R.string.dialog_alert_error_title))
        setMessage(message ?: getString(R.string.dialog_alert_error_message))
        setPositiveButton(R.string.close) { _, _ ->
            if (onClose != null) {
                onClose()
            }
        }
        setOnDismissListener {
            if (onClose != null) {
                onClose()
            }
        }

        show()
    }
}

