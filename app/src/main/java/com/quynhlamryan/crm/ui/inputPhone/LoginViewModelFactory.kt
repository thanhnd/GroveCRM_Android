package com.quynhlamryan.crm.ui.inputPhone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.data.LoginDataSource
import com.quynhlamryan.crm.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputPhoneViewModel::class.java)) {
            return InputPhoneViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}