package com.quynhlamryan.crm.ui.notify

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.NotifyRepository
import com.quynhlamryan.crm.data.model.Notify

class NotifyViewModel : ViewModel() {

    fun getListNotify() : LiveData<Notify>? {
        return NotifyRepository.getListNotify()
    }
}