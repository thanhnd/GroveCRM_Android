package com.grove.crm.ui.membercard

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.grove.crm.R
import com.grove.crm.utils.AccountManager
import com.grove.crm.utils.BaseActivity
import com.grove.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_member_card.*


class MemberCardActivity : BaseActivity() {
    var screenBrightness: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_card)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = getString(R.string.member_card)

        AccountManager.account?.let { account ->
            val code = account.userName ?: return
            val bitmap = generateQRCode(code)
            ivCode.setImageBitmap(bitmap)
            tvCode.text = code

        }

        val lp = window.attributes
        screenBrightness = lp.screenBrightness
        lp.screenBrightness = 1F
        window.attributes = lp
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        screenBrightness?.let {
            val lp = window.attributes
            lp.screenBrightness = it
            window.attributes = lp
        }

        super.onDestroy()
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Logger.d("generateQRCode: ${e.message}")
        }
        return bitmap
    }
}