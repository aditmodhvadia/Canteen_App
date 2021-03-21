package com.example.getfood.ui.map

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.getfood.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        // Add a marker in K Canteen and move the camera
        val KCanteen = LatLng(23.1282703, 72.5435537)
        googleMap.addMarker(MarkerOptions().position(KCanteen).title(getString(R.string.k_canteen_title)))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(KCanteen, 18f), 3000, null)
    }
}