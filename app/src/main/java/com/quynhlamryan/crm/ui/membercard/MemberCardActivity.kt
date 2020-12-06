package com.quynhlamryan.crm.ui.membercard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_member_card.*

class MemberCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_card)

        AccountManager.account?.let {account ->
            Glide
                .with(this)
                .load(account.urlBarcode)
            .into(ivCode)
        }
    }
}