package com.quynhlamryan.crm.ui.membercard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_member_card.*


class MemberCardActivity : AppCompatActivity() {
    var screenBrightness: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_card)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        AccountManager.account?.let { account ->
            Glide
                .with(this)
                .load(account.urlBarcode)
            .into(ivCode)
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
}