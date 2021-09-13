package com.grove.crm.utils

import com.grove.crm.Constants
import com.grove.crm.Constants.PREF_NAME
import com.grove.crm.Constants.PRIVATE_MODE
import com.grove.crm.MyApplication
import com.grove.crm.data.model.Account
import com.grove.crm.data.model.Config

object AccountManager {

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
            field = value
        }

    var fcmToken: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                return sharedPref?.getString(Constants.fcmTokenKey, null)
            }

            return field
        }
        set(value) {
            if (value.isNullOrEmpty()) {
                sharedPref?.edit()?.remove(Constants.fcmTokenKey)?.apply()
            } else {
                sharedPref?.edit()?.putString(Constants.fcmTokenKey, value)?.apply()
            }
            field = value
        }

    fun logout() {
        account = null
        token = null
        config = null
        accountCode = null
    }

}