package ingsw.mobile

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import ingsw.mobile.authentication.AuthFactory
import ingsw.mobile.authentication.AuthProvider
import ingsw.mobile.controller.HomeController
import ingsw.mobile.controller.MapController
import ingsw.mobile.fragment.HomeFragment
import ingsw.mobile.fragment.WriteRecFragment
import ingsw.mobile.R

class HomeActivity: AppCompatActivity() {
    private lateinit var mapController: MapController
    private lateinit var authProvider: AuthProvider
    private lateinit var homeController: HomeController
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(!isTaskRoot) {
            finish()
        }

        mapController = MapController(this)
        authProvider = AuthFactory(this).getAuthProvider()
        homeController = HomeController(this, authProvider)

        handleIntent(intent)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container_fragment, HomeFragment(homeController),"home").commit()
        }
    }

    override fun onStart() {
        super.onStart()
        if(menu != null) {
            impostaTitoloMenu(menu)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            homeController.doResearch(query?:"")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_layout, menu)
        this.menu = menu

        impostaTitoloMenu(menu)
        impostaRicercaMenu(menu)

        return true
    }

    fun abilitaRicerca(abilitato: Boolean){
        if(menu != null) {
            val bottoneRicerca = menu!!.findItem(R.id.menu_ricerca)
            bottoneRicerca.isVisible = abilitato
        }
    }

    private fun impostaRicercaMenu(menu: Menu?) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.menu_ricerca).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
    }

    private fun impostaTitoloMenu(menu: Menu?) {
        val menuLogout = menu!!.findItem(R.id.menu_logout)
        menuLogout.setOnMenuItemClickListener(homeController)
        if(authProvider.actuallyAuth()) {
            menuLogout.setTitle(R.string.logout)
        }
        else{
            menuLogout.setTitle(R.string.accedi)
        }
    }

    fun homePremuto(item: MenuItem?) {
        if (fragmentCorrenteScriviRecensione()) {
            mostraPopupTornaAllaHome()
        }
        else{
            homeController.backFromHome()
        }
    }

    private fun fragmentCorrenteScriviRecensione(): Boolean {
        val fragmentAttuale = supportFragmentManager.findFragmentByTag("scriviRecensione")
        if (fragmentAttuale != null &&
            fragmentAttuale.isVisible &&
            fragmentAttuale is WriteRecFragment &&
            fragmentAttuale.almenoUnCampoRiempito()) {
            return true
        }
        return false
    }

    private fun mostraPopupTornaAllaHome() {
        val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.popup_titolo_attenzione)
                .setMessage(R.string.popup_torna_alla_home_perdita_dati)
                .setPositiveButton(R.string.bottone_conferma) { _, _ -> homeController.backFromHome() }
                .setNegativeButton(R.string.bottone_annulla) { dialog, _ -> dialog.dismiss()}
                .show()
    }

    fun nascondiSearchBar() {
        val searchView = menu!!.findItem(R.id.menu_ricerca).actionView as SearchView
        searchView.setQuery("", false)
        searchView.isIconified = true
    }

    fun rimuoviFocusDaSearchBar() {
        val searchView = menu!!.findItem(R.id.menu_ricerca).actionView
        searchView.clearFocus()
    }
}
