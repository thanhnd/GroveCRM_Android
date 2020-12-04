package com.quynhlamryan.crm.ui.mapstore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quynhlamryan.crm.data.MapStoreActivityRepository
import com.quynhlamryan.crm.data.model.Store

class MapStoreActivityViewModel : ViewModel() {

    var ldStores: MutableLiveData<List<Store>>? = null

    fun getStores() : LiveData<List<Store>>? {
        ldStores = MapStoreActivityRepository.getListStores()
        return ldStores
    }

}