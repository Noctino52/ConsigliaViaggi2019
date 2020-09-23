package ingsw.mobile.controller

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.dao.DAOFactory
import ingsw.mobile.entity.SearchFilter
import ingsw.mobile.entity.Structure
import ingsw.mobile.fragment.FilterSearchFragment
import ingsw.mobile.fragment.SearchFragment
import ingsw.mobile.fragment.StructureFragment
import java.util.Locale
import kotlin.collections.HashSet

class SearchController(private val homeActivity: HomeActivity,
                       private val searchFragment: SearchFragment) {
    private var filtro = SearchFilter(3, 0, Float.MAX_VALUE, GpsOn() && allowGPSPermission(), "")
    private var setStrutture: MutableSet<Structure> = HashSet()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(homeActivity)

    fun getStructure(query: String) {
        if (setStrutture.isEmpty()){
            class OttieniStruttureAsync: AsyncTask<Void, Void, MutableSet<Structure>>() {
                val daoFactory = DAOFactory(homeActivity)
                val strutturaDAO = daoFactory.getStrutturaDAO()

                override fun doInBackground(vararg params: Void?): MutableSet<Structure> {
                    return HashSet(strutturaDAO.getAllStructure())
                }

                override fun onPostExecute(result: MutableSet<Structure>?) {
                    if(result == null) { return }
                    addDistance(result, query)
                }
            }
            OttieniStruttureAsync().execute()
        }
        else {
            doSearch(query)
        }
    }
    fun doSearch(query: String) {
        searchFragment.setTextViewRisultatiRicerca(query)

        var struttureValide = if(query == "Ristorante" || query == "AttrazioneTuristica" || query == "Hotel") {
            findStructureWithTypeStructure(query)
        }
        else {
            findStructureWithName(formatStringForSearch(query))
        }
        struttureValide = filterWithMinStar(struttureValide, filtro.starMin)
        struttureValide = filterWithMaxDistance(struttureValide, filtro.maxKM)

        struttureValide = if(filtro.gpsON && filtro.sort == 3) {
            orderByDistance(struttureValide)
        }
        else if (filtro.sort == 3 || filtro.sort == 2) {
            orderByDecrescendingStars(struttureValide)
        }
        else {
            orderByCrescendingStars(struttureValide)
        }

        searchFragment.setListViewStrutture(struttureValide)
    }


    private fun distanceBetweenUserAndStructure(posUtente: Location, structure: Structure): Float {
        val distanza = FloatArray(1)
        Location.distanceBetween(structure.latitudine, structure.longitudine,
            posUtente.latitude, posUtente.longitude, distanza)
        return distanza[0]
    }

    private fun addDistance(setStrutture: Set<Structure>, query: String) {
        if(filtro.gpsON) {
            fillWithDistanceStructureUser(setStrutture, query)
        }
        else {
            fillWithZeroDistance(setStrutture)
            this.setStrutture = HashSet(setStrutture)
            doSearch(query)
        }
    }

    private fun formatStringForSearch(stringa: String): String {
        return stringa.toLowerCase(Locale.getDefault()).trim().replace(" +".toRegex(), " ")
    }

    private fun findStructureWithTypeStructure(tipoStruttura: String): List<Structure> {
        return setStrutture.filter { it.tipoStruttura.contains(tipoStruttura) }
    }

    private fun findStructureWithName(nome: String): List<Structure> {
        return setStrutture.filter {
            it.nomeStruttura.toLowerCase(Locale.getDefault()).contains(nome)
        }
    }

    private fun filterWithMinStar(listaStrutture: List<Structure>,
                                  stelleMinime: Int): List<Structure> {
        return listaStrutture.filter { it.mediaRecensioni!! >= stelleMinime }
    }

    private fun filterWithMaxDistance(listaStrutture: List<Structure>,
                                      distanzaMax: Float): List<Structure> {
        return listaStrutture.filter { it.distanzaDaUtente!! <= distanzaMax }
    }

    private fun orderByDistance(listaStrutture: List<Structure>): List<Structure> {
        return listaStrutture.sortedBy { it.distanzaDaUtente }
    }

    private fun orderByDecrescendingStars(listaStrutture: List<Structure>): List<Structure> {
        return listaStrutture.sortedByDescending { it.mediaRecensioni }
    }

    private fun orderByCrescendingStars(listaStrutture: List<Structure>): List<Structure> {
        return listaStrutture.sortedBy { it.mediaRecensioni }
    }

    private fun openScreen(fragmentClass: Fragment) {
        homeActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, fragmentClass)
            .addToBackStack(null).commit()
    }

    fun openStructure(structure: Structure) {
        openScreen(StructureFragment(structure))
    }

    fun openFilter(query: String) {
        filtro.query = query

        // Verifico nuovamente se le condizioni per il gps attivo sono rispettate
        filtro.gpsON = allowGPSPermission() && GpsOn()

        val fragmentFiltriRicerca = FilterSearchFragment(filtro, this)
        openScreen(fragmentFiltriRicerca)
    }

    private fun allowGPSPermission(): Boolean {
        val permission = "android.permission.ACCESS_FINE_LOCATION"
        val res = homeActivity.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun GpsOn(): Boolean {
        val manager = homeActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }



    private fun fillWithZeroDistance(setStrutture: Set<Structure>) {
        for (struttura in setStrutture) {
            struttura.distanzaDaUtente = 0f
        }
    }
    private fun fillWithDistanceStructureUser(setStrutture: Set<Structure>, query: String) {
        fusedLocationClient.lastLocation.addOnSuccessListener { posizioneUtente: Location? ->
            if (posizioneUtente == null) {
                fillWithZeroDistance(setStrutture)
            }
            else {
                fillWithEffectiveDistance(setStrutture, posizioneUtente)
            }

            this.setStrutture = HashSet(setStrutture)
            doSearch(query)
        }
    }

    private fun fillWithEffectiveDistance(setStrutture: Set<Structure>, posUtente: Location) {
        for (struttura in setStrutture) {
            struttura.distanzaDaUtente = distanceBetweenUserAndStructure(posUtente, struttura)
        }
    }
}