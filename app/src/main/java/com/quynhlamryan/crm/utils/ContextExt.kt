package com.quynhlamryan.crm.utils

import android.app.AlertDialog
import android.content.Context
import com.quynhlamryan.crm.R

fun Context.showAlertDialog(title: String? = null, message: String? = null) {
    AlertDialog.Builder(this).apply {
        setTitle(title ?: getString(R.string.dialog_alert_error_title))
        setMessage(message ?: getString(R.string.dialog_alert_error_message))
        show()
    }
}

