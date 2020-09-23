package ingsw.mobile.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.authentication.AuthFactory
import ingsw.mobile.dao.DAOFactory
import ingsw.mobile.entity.Review
import ingsw.mobile.entity.Structure
import ingsw.mobile.entity.User
import ingsw.mobile.fragment.MapFragment
import ingsw.mobile.fragment.WriteRecFragment
import ingsw.mobile.fragment.FilterRecFragment
import ingsw.mobile.fragment.StructureFragment
import java.util.Date

class StructureController(private val structureFragment: StructureFragment,
                          private val activity: Context,
                          private val structure: Structure) {
    private var stelleValide = arrayOf(true, true, true, true, true)
    private var recensioneDAO = DAOFactory(activity).getRecensioneDAO()
    private lateinit var recensioniTotali: List<Review>
    private var ordinamento = 1
    private var immagine: Bitmap? = null
    private var caricataListaRecensioni = false
    var writeRecFragment: WriteRecFragment? = null

    private var userCanWriteReview = false
        set(value) {
            if(value) {
                structureFragment.impostaMessaggioErroreRecensione(R.string.stringa_vuota)
                structureFragment.abilitaBottoneScrittura()
            }
            else {
                structureFragment.disabilitaBottoneScrittura()
            }
            field = value
        }

    fun openScreenFilter() {
        openScreen(FilterRecFragment(this), "filtri")
    }

    fun setFilter(ordinamento: Int, stelleValide: Array<Boolean>) {
        this.ordinamento = ordinamento
        this.stelleValide = stelleValide
    }

    fun fillAsyncReview(structure: Structure) {
        class CaricaRecensioniAsync: AsyncTask<Any, Any, List<Review>>() {
            override fun doInBackground(vararg params: Any?): List<Review> {
                return recensioneDAO.getReviewStructure(structure)
            }

            override fun onPostExecute(result: List<Review>?) {
                if(result == null) { return }
                recensioniTotali = result
                caricataListaRecensioni = true

                assignReviewOnStructure()
            }
        }
        CaricaRecensioniAsync().execute()
    }

    private fun assignReviewOnStructure() {
        userCanWriteReview = verifyIfUserCanWriteRec()

        // La media è calcolata su tutte le recensioni approvate, indipendentemente dai filtri
        val mediaStruttura = averageStarReview(recensioniTotali)
        structureFragment.impostaValutazione(mediaStruttura)

        val recensioniFiltrate = applyFilterReview(recensioniTotali)
        structureFragment.impostaListViewRecensioni(recensioniFiltrate)
    }

    fun fillSelectedStructure() {
        verifiyIfButtonWriteRecShouldBeOn()

        if(caricataListaRecensioni) {
            assignReviewOnStructure()
        }
    }
    fun verifiyIfButtonWriteRecShouldBeOn() {
        val gestoreAutenticazione = AuthFactory(activity).getAuthProvider()
        val nomeUtenteAttuale = gestoreAutenticazione.obtainUsername()
        if(!nomeUtenteAttuale.isNullOrBlank() && caricataListaRecensioni) {
            userCanWriteReview = verifyIfUserCanWriteRec()
        }
    }

    private fun verifyIfUserCanWriteRec(): Boolean {
        val gestoreAutenticazione = AuthFactory(activity).getAuthProvider()
        val nomeUtenteAttuale = gestoreAutenticazione.obtainUsername()
        if(nomeUtenteAttuale.isNullOrBlank()) {
            structureFragment.impostaMessaggioErroreRecensione(
                R.string.avviso_scrittura_devi_effettuare_il_login)
            return false
        }

        if(reviewAlreadyWrite(recensioniTotali, nomeUtenteAttuale)) {
            structureFragment.impostaMessaggioErroreRecensione(
                R.string.avviso_scrittura_hai_gia_scritto_una_recensione)
            return false
        }

        return true
    }

    private fun reviewAlreadyWrite(recensioni: List<Review>,
                                   nomeUtenteAutore: String): Boolean {
        return recensioni.find { it.autore.nomeUtente == nomeUtenteAutore } != null
    }

    private fun averageStarReview(recensioni: List<Review>): Float {
        if(recensioni.isNotEmpty()) {
            var mediaStruttura = 0f

            for(recensione in recensioni) {
                mediaStruttura += recensione.valutazione
            }

            return mediaStruttura / recensioni.size
        }
        return 0f
    }

    private fun openScreen(fragmentClass: Fragment, tag: String) {
        (activity as HomeActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, fragmentClass, tag)
            .addToBackStack(null).commit()
    }

    private fun applyFilterReview(recensioni: List<Review>): List<Review> {
        val recensioniFiltrate = recensioni.filter {
            (it.valutazione == 1 && stelleValide[0])
                .xor(it.valutazione == 2 && stelleValide[1])
                .xor(it.valutazione == 3 && stelleValide[2])
                .xor(it.valutazione == 4 && stelleValide[3])
                .xor(it.valutazione == 5 && stelleValide[4])
        }

        return when(ordinamento) {
            2 -> recensioniFiltrate.sortedBy { it.dataCreazione }
            3 -> recensioniFiltrate.sortedBy { it.valutazione }
            4 -> recensioniFiltrate.sortedByDescending { it.valutazione }
            else -> recensioniFiltrate.sortedByDescending { it.dataCreazione }
        }
    }

    fun openMapWithStructure(structure: Structure) {
        openScreen(MapFragment(MapController(activity as HomeActivity), structure), "mappa")
    }

    fun openStructureNewReview() {
        openScreen(WriteRecFragment(this, structure), "scriviRecensione")
    }

    fun pubblishNewReview(valutazione: Int, testo: String, structure: Structure) {
        // L'utente viene identificato dal server, la structure dalla terna
        val utenteStub = User("", "")
        val strutturaStub = Structure(structure.nomeStruttura,
            structure.latitudine, structure.longitudine)
        val recensione = Review(valutazione, testo, Date(), utenteStub, strutturaStub)

        class PubblicaRecensione: AsyncTask<Any, Any, Boolean>() {
            override fun doInBackground(vararg params: Any?): Boolean {
                return recensioneDAO.addNewReview(recensione)
            }

            override fun onPostExecute(result: Boolean?) {
                if(result == null || !result) {
                    writeRecFragment!!.mostraPopup(R.string.popup_titolo_errore,
                        R.string.si_e_verificato_un_errore_durante_la_pubblicazione)
                }
                else {
                    writeRecFragment!!.mostraPopup(R.string.popup_titolo_successo,
                        R.string.pubblicazione_effettuata_con_successo)
                    fillAsyncReview(structure)
                }
            }
        }
        PubblicaRecensione().execute()
    }
}