package com.quynhlamryan.crm.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.quynhlamryan.crm.R

object CustomProgressDialog {
    private var progressDialog: Dialog? = null

    fun showProgressDialog(mContext: Context?) {
        Logger.d("Show progress dialog")
        dismissProgressDialog()
        progressDialog = null
        System.gc()
        if (mContext != null) {
            progressDialog = Dialog(mContext)
            val inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_progress, null)
            progressDialog!!.setContentView(inflate)
            progressDialog!!.show()
            if (progressDialog!!.window != null) {
                progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            progressDialog!!.setCancelable(false)
        }
    }

    fun dismissProgressDialog() {
        Logger.d("Hide progress dialog")
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                Logger.e(e)
            }

        }
    }

    @SuppressLint("Unused")
    fun setProgressDialogText(text: String) {
        val textView = progressDialog!!.findViewById<TextView>(R.id.textView1)
        textView.text = text
    }

}
