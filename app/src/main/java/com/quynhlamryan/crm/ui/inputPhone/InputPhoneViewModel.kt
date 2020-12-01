package com.quynhlamryan.crm.ui.inputPhone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.quynhlamryan.crm.data.LoginRepository
import com.quynhlamryan.crm.data.Result

import com.quynhlamryan.crm.R

class InputPhoneViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<InputPhoneNumberFormState>()
    val inputPhoneNumberFormState: LiveData<InputPhoneNumberFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun inputPhone(phone: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.sendPhoneNumber(phone)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(phone: String) {
        if (!isUserNameValid(phone)) {
            _loginForm.value = InputPhoneNumberFormState(phoneError = R.string.invalid_username)
        }else {
            _loginForm.value = InputPhoneNumberFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}