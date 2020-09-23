package ingsw.mobile.controller

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.dao.DAOFactory
import ingsw.mobile.entity.Structure
import ingsw.mobile.fragment.MapFragment
import ingsw.mobile.fragment.StructureFragment

class MapController(private val homeActivity: HomeActivity):

    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {
    private var strutture: MutableMap<Structure, Structure> = HashMap()
    private val lunghezzaDescrizioneStruttura = 40
    private var listaMarker: MutableList<MarkerOptions>? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mapFragment: MapFragment
    lateinit var mappa: GoogleMap


    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(homeActivity, R.string.bottone_posizione_premuto_toast, Toast.LENGTH_SHORT)
            .show()
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val posizione = marker!!.position
        val strutturaDaAprire = Structure(marker.title, posizione.latitude, posizione.longitude)
        openInfoStructure(strutture[strutturaDaAprire])
    }


    private fun inializzationBaseMap() {
        mappa.setOnInfoWindowClickListener(this)

        // Prendo la posizione dell'utente
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(
            this@MapController.homeActivity
        )
    }

    fun loadPinsFromMap() {
        inializzationBaseMap()

        addZoomToUser(10f, 3000)

        class OttieniStruttureAsync: AsyncTask<Void, Void, Set<Structure>>() {
            val strutturaDAO = DAOFactory(homeActivity).getStrutturaDAO()

            override fun doInBackground(vararg params: Void?): Set<Structure> {
                return HashSet(strutturaDAO.getAllStructure())
            }

            override fun onPostExecute(result: Set<Structure>?) {
                addAllMarkerOnMap(result)
            }
        }
        OttieniStruttureAsync().execute()
    }

    fun addZoomToUser(distanzaZoom: Float, ms: Int) {
        fusedLocationClient.lastLocation.addOnSuccessListener { posizioneUtente: Location? ->
            if(posizioneUtente != null) {
                val latLng = LatLng(posizioneUtente.latitude, posizioneUtente.longitude)
                addZoomOnCoordinate(latLng, ms, distanzaZoom)
            }
        }
    }

    fun loadSinglePinsOnMap(structure: Structure) {
        inializzationBaseMap()

        val latLng = LatLng(structure.latitudine, structure.longitudine)
        addZoomOnCoordinate(latLng, 3000, 10f)

        val marker = createMarkerFromStructure(structure)
        mappa.addMarker(marker)
        strutture[structure] = structure
    }

    private fun addZoomOnCoordinate(latLng: LatLng, tempoInMs: Int, distanzaZoom: Float) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(distanzaZoom).build()
        mappa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), tempoInMs, null)
    }

    fun addAllMarkerOnMap(struttureDaAggiungere: Set<Structure>?) {
        if(struttureDaAggiungere == null) { return }

        listaMarker = ArrayList()
        for (struttura in struttureDaAggiungere) {
            strutture[struttura] = struttura
            val marker = createMarkerFromStructure(struttura)
            listaMarker!!.add(marker)
            mappa.addMarker(marker)
        }
    }

    fun refreshMarkerOnMap(distanzaMax: Double) {
        val zoom = when(distanzaMax) {
            1000.0 -> 15f
            3000.0 -> 13.5f
            5000.0 -> 12f
            10000.0 -> 11f
            else -> 9f
        }
        addZoomToUser(zoom, 1500)

        fusedLocationClient.lastLocation.addOnSuccessListener { posizioneUtente: Location? ->
            recalculateMarkerOnMap(posizioneUtente, distanzaMax)
        }
    }


    private fun letGpsOn(): Boolean {
        val permission = "android.permission.ACCESS_FINE_LOCATION"
        val res = homeActivity.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun allowGps() {
        mappa.isMyLocationEnabled = true
        mappa.setOnMyLocationButtonClickListener(this)
        buttonDownMyLocation()
        Toast.makeText(homeActivity, R.string.funzioni_gps_attive_toast, Toast.LENGTH_SHORT).show()
    }

    private fun buttonDownMyLocation() {
        val locationButton = (homeActivity.findViewById<View>(Integer.parseInt("1")).parent as
                View).findViewById<View>(Integer.parseInt("2"))
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 0, 150)
    }

    private fun gpsAttivo(): Boolean {
        val manager = homeActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun allowGpsOnlyIf() {
        if(!letGpsOn()) {
            mapFragment.showPopupPermission()
        }
        else if(!gpsAttivo()) {
            mapFragment.showPopupRequestGps()
        }
        else {
            allowGps()
        }
    }

    private fun recalculateMarkerOnMap(posizioneUtente: Location?, distanzaMax: Double) {
        if(posizioneUtente == null || listaMarker == null) { return }

        mappa.clear()
        for(marker in listaMarker!!) {
            if (distanceValidBetweenUserAndMarker(posizioneUtente, marker, distanzaMax)) {
                mappa.addMarker(marker)
            }
        }
    }

    private fun distanceValidBetweenUserAndMarker(posUtente: Location?, marker: MarkerOptions,
                                                  distanzaMax: Double): Boolean {
        val distanza = FloatArray(1)
        Location.distanceBetween(marker.position.latitude, marker.position.longitude,
            posUtente?.latitude!!, posUtente.longitude, distanza)
        return distanza[0] <= distanzaMax
    }

    private fun createMarkerFromStructure(structure: Structure): MarkerOptions {
        val pinHotel = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        val pinRistorante = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        val pinAttrazione = BitmapDescriptorFactory.defaultMarker(50f) // Giallo

        val marker = MarkerOptions()
            .position(LatLng(structure.latitudine, structure.longitudine))
            .title(structure.nomeStruttura)
            .snippet(createDescriptionMarkerFromStructure(structure))

        when (structure.tipoStruttura) {
            "Hotel" -> marker.icon(pinHotel)
            "Ristorante" -> marker.icon(pinRistorante)
            else -> marker.icon(pinAttrazione)
        }
        return marker
    }

    private fun createDescriptionMarkerFromStructure(structure: Structure): String {
        var descrizione = if(structure.tipoStruttura == "AttrazioneTuristica") {
            "Attrazione Turistica"
        }
        else {
            structure.tipoStruttura
        }

        descrizione += ", ${structure.indirizzo}"
        return stopDescription(descrizione)
    }

    private fun stopDescription(descrizione: String): String {
        return if(descrizione.length <= lunghezzaDescrizioneStruttura) {
            descrizione
        }
        else {
            "${descrizione.substring(0, lunghezzaDescrizioneStruttura-3)}..."
        }
    }

    private fun openInfoStructure(structure: Structure?) {
        if(structure == null) { return }
        homeActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, StructureFragment(structure))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .addToBackStack(null).commit()
    }
}
