package ingsw.mobile.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.controller.MapController
import ingsw.mobile.databinding.FragmentMapBinding
import ingsw.mobile.entity.Structure

class MapFragment(private val mapController: MapController,
                  private val selectedStructureMap: Structure? = null): Fragment(),
    OnMapReadyCallback {
    private var map: GoogleMap? = null
    private lateinit var binding: FragmentMapBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapController.mapFragment = this
        (activity as HomeActivity).abilitaRicerca(true)

        binding.apply {
            bottonMostra1000m.setOnClickListener { mapController.refreshMarkerOnMap(1000.0) }
            buttonMostra2500m.setOnClickListener { mapController.refreshMarkerOnMap(3000.0) }
            buttonMostra5000m.setOnClickListener { mapController.refreshMarkerOnMap(5000.0) }
            buttonMostra10km.setOnClickListener { mapController.refreshMarkerOnMap(10000.0) }
            buttonMostraTutto.setOnClickListener { mapController.refreshMarkerOnMap(Double.MAX_VALUE) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return  binding.root
    }

    override fun onStart() {
        super.onStart()
        if(map != null) {
            mapController.allowGpsOnlyIf()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map!!
        mapController.mappa = this.map!!
        binding.buttonMostraTutto.isChecked = true
        if(selectedStructureMap == null) {
            binding.toggleGroup.isVisible = true
            mapController.loadPinsFromMap()
        }
        else {
            binding.toggleGroup.isVisible = false
            mapController.loadSinglePinsOnMap(selectedStructureMap)
        }
        mapController.allowGpsOnlyIf()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(setPermission(requestCode, grantResults)) {
            mapController.addZoomToUser(10f, 3000)
            mapController.allowGpsOnlyIf()
        }
    }

    fun showPopupPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    fun showPopupRequestGps() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.popup_titolo_attenzione)
            .setMessage(R.string.gps_disabilitato_richiesta_abilitazione)
            .setCancelable(false)
            .setPositiveButton(R.string.bottone_conferma) { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(R.string.bottone_annulla) { dialog, _ -> dialog.dismiss() }
            .show()
    }


    private fun setPermission(requestCode: Int, grantResults: IntArray): Boolean {
        return requestCode == 1 && grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
}