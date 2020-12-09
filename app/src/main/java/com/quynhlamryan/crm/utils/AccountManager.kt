package com.quynhlamryan.crm.utils

import com.quynhlamryan.crm.Constants
import com.quynhlamryan.crm.MyApplication
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.Config

object AccountManager {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "QuynhLamRyan"
    private val sharedPref = MyApplication.instance?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    var accountCode: String? = null
    var config: Config? = null
    var account: Account? = null
//    static var accountCode: String? = nil
    var phone: String? = null
    var token: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                return sharedPref?.getString(Constants.accountTokenKey, null)
            }

            return field
        }
        set(value) {
            if (value.isNullOrEmpty()) {
                sharedPref?.edit()?.remove(Constants.accountTokenKey)?.apply()
            } else {
                sharedPref?.edit()?.putString(Constants.accountTokenKey, value)?.apply()
            }

        }

    fun logout() {
        account = null
        token = null
        config = null
        accountCode = null
    }

}