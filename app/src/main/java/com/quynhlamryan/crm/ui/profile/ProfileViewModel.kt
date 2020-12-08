package com.quynhlamryan.crm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.ProfileRepository
import com.quynhlamryan.crm.data.request.AccountRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProfileViewModel : ViewModel() {

    var ldUpdateAccountResult: MutableLiveData<Boolean>? = null
    var ldUpdateAvatarResult: MutableLiveData<Boolean>? = null

    fun updateAccount(name: String?, birthday: String?, email: String?) : LiveData<Boolean>? {

        val request = AccountRequest(name, email, birthday)
        this.ldUpdateAccountResult = ProfileRepository.updateProfile(request)
        return this.ldUpdateAccountResult
    }

    fun uploadAvatar(filePath: String): LiveData<Boolean>? {
        val file = File(filePath)
        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaType())
        )

        this.ldUpdateAvatarResult = ProfileRepository.uploadAvatar(filePart)
        return this.ldUpdateAvatarResult
    }
}