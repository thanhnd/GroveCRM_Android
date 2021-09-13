package com.grove.crm.ui.notify

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.grove.crm.data.NotifyRepository
import com.grove.crm.data.model.Notify

class NotifyViewModel : ViewModel() {

    fun getListNotify() : LiveData<Notify>? {
        return NotifyRepository.getListNotify()
    }
}