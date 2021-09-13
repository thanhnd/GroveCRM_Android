package com.grove.crm.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class DialogUtil {
    fun showInformationAlertDialog(
        context: Context?, icon: Int, msg: String, positiveBtnMessage: String,
        cancelable: Boolean, dialogClickListener: DialogClickListener?
    ) {
        val context = context ?: return

        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setCancelable(cancelable)
        alertDialogBuilder.setMessage(msg)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    interface DialogClickListener {
        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()
    }
}