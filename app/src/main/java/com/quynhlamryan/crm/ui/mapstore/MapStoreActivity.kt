package com.quynhlamryan.crm.ui.mapstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Store
import com.quynhlamryan.crm.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_map_store.*

class MapStoreActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapStoreStoreViewMdodel: MapStoreActivityViewModel
    private val adapter = StoreAdapter()
    private var map: GoogleMap? = null
    private var stores: List<Store> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_store)
        mapStoreStoreViewMdodel = ViewModelProvider(this).get(MapStoreActivityViewModel::class.java)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter.onItemClick = { store ->
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(store.latitude, store.longitude), 17.0f))

        }
        rvStore.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        CustomProgressDialog.showProgressDialog(this)
        mapStoreStoreViewMdodel.getStores()?.observe(this, Observer { stores ->
            CustomProgressDialog.dismissProgressDialog()
            adapter.stores = stores

            this.stores = stores
            showMap()
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.apply {
            map = googleMap
            showMap()
        }

    }

    private fun showMap() {
        if (stores.isNotEmpty() && map != null) {
            stores.forEach { store ->
                map?.addMarker(
                    MarkerOptions()
                        .position(LatLng(store.latitude, store.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_store))
                        .title(store.storeName)
                )
            }
            stores.firstOrNull()?.let { store ->
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(store.latitude, store.longitude), 16.0f))
            }
        }
    }
}