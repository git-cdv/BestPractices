package com.chkan.bestpractices.location_tracking

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentLocationBinding
import com.chkan.bestpractices.location_tracking.LocationService.Companion.BR_ACTION
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationFragment : BaseFragment<FragmentLocationBinding>(FragmentLocationBinding::inflate) {

    private var mapFragment: SupportMapFragment? = null
    private var mapObject: GoogleMap? = null
    private val locationReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                onMap(intent?.extras?.getParcelable(LocationService.LOCATION_EXTRA))
            }
        }
    }

    private fun onMap(latLng: LatLng?) {
        latLng?.let {
            mapObject?.clear()
            mapObject?.addMarker(
                MarkerOptions()
                    .position(it))
            mapObject?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        binding.btnStart.setOnClickListener {
            startServiceWithAction(LocationService.ACTION_START)
        }
        binding.btnStop.setOnClickListener {
            startServiceWithAction(LocationService.ACTION_STOP)
        }

        initMap()
    }

    private fun initMap() {
        mapFragment = SupportMapFragment.newInstance()
        mapFragment?.getMapAsync { map ->
            mapObject = map
            val latLngWork = LatLng(47.84303067630826, 35.13851845689717)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngWork, 15f))
        }
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment!!).commit()
    }

    private fun startServiceWithAction(act : String){
        context?.startService(
            Intent(context, LocationService::class.java).apply { action = act }
        )
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(locationReceiver, IntentFilter().apply { addAction(BR_ACTION) })
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(locationReceiver)
    }

}