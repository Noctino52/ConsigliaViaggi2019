package ingsw.mobile.fragment

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.controller.StructureController
import ingsw.mobile.databinding.FragmentRecensioniBinding
import ingsw.mobile.entity.Structure

class WriteRecFragment(private val structureController: StructureController,
                       private val structure: Structure): Fragment() {
    private lateinit var binding: FragmentRecensioniBinding
    private var rating: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as HomeActivity).abilitaRicerca(false)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_recensioni, container, false)
        impostaGrafica()
        structureController.writeRecFragment = this
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ -> blockTornaIndietroSeCampiPieni(keyCode) }

        binding.editTextTestoRecensione.setOnKeyListener { _, keyCode, _ ->
            blockTornaIndietroSeCampiPieni(keyCode)
        }
    }

    private fun impostaGrafica() {
        binding.apply {
            textViewNomeStrutturaScriviRecensione.text = structure.nomeStruttura
            val stringaCaratteriMancanti = activity!!
                .resources.getString(R.string.mancano_d_caratteri)
            textViewNumeroCaratteriRecensione.text = String.format(stringaCaratteriMancanti, 100)
        }

        setEditText()
        setButton()
        verificaEdAttivaBottonePubblica()
    }


    private fun blockTornaIndietroSeCampiPieni(keyCode: Int): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && almenoUnCampoRiempito() && !popupAperto) {
            mostraPopupErroreBackPremuto()
            true
        }
        else {
            false
        }
    }

    private fun setEditText() {
        binding.editTextTestoRecensione.doOnTextChanged { text, _, _, _ ->
            if(text!!.trim().length < 100) {
                binding.textViewNumeroCaratteriRecensione.isVisible = true
                val caratteriMancanti: Int = (100 - text.trim().length)
                val stringaCaratteriMancanti = activity!!
                    .resources.getString(R.string.mancano_d_caratteri)
                binding.textViewNumeroCaratteriRecensione.text =
                    String.format(stringaCaratteriMancanti, caratteriMancanti)
            }
            else {
                binding.textViewNumeroCaratteriRecensione.isVisible = false
            }
            verificaEdAttivaBottonePubblica()
        }
    }

    private fun setButton() {
        binding.apply {
            buttonUnaStella.setOnClickListener { stellaPremuta(1) }
            buttonDueStelle.setOnClickListener { stellaPremuta(2) }
            buttonTreStelle.setOnClickListener { stellaPremuta(3) }
            buttonQuattroStelle.setOnClickListener { stellaPremuta(4) }
            buttonCinqueStelle.setOnClickListener { stellaPremuta(5) }
            buttonPubblicaRecensione.setOnClickListener { pubblicaRecensionePremuto() }
            buttonAnnullaRecensione.setOnClickListener { annullaPremuto() }
        }
        setStar(0)
    }

    private fun stellaPremuta(valutazione: Int) {
        this.rating = valutazione
        verificaEdAttivaBottonePubblica()
        setStar(valutazione)
    }

    private fun setStar(valutazione: Int) {
        binding.apply {
            buttonUnaStella.background = activity!!.getDrawable(getStar( 1, valutazione))
            buttonDueStelle.background = activity!!.getDrawable(getStar( 2, valutazione))
            buttonTreStelle.background = activity!!.getDrawable(getStar( 3, valutazione))
            buttonQuattroStelle.background = activity!!.getDrawable(getStar( 4, valutazione))
            buttonCinqueStelle.background = activity!!.getDrawable(getStar( 5, valutazione))
        }
    }

    private fun getStar(numeroStella: Int, valutazione: Int): Int {
        return if(numeroStella <= valutazione) {
            R.drawable.ic_star_black_24dp
        }
        else {
            R.drawable.ic_star_border_black_24dp
        }
    }

    private fun verificaEdAttivaBottonePubblica() {
        binding.buttonPubblicaRecensione.isEnabled = recensioneValida()
    }

    private fun annullaPremuto() {
        if(almenoUnCampoRiempito()) {
            mostraPopupErroreBackPremuto()
        }
        else {
            activity!!.onBackPressed()
        }
    }

    private fun pubblicaRecensionePremuto() {
        binding.buttonPubblicaRecensione.isEnabled = false
        val testo = binding.editTextTestoRecensione.text.trim().toString()
        structureController.pubblishNewReview(rating!!, testo, structure)
    }


    fun almenoUnCampoRiempito(): Boolean {
        return rating != null || binding.editTextTestoRecensione.text.trim().isNotEmpty()
    }

    private fun recensioneValida(): Boolean {
        return rating != null && binding.editTextTestoRecensione.text.trim().length >= 100
    }

    fun mostraPopup(idTitolo: Int, idMessaggio: Int) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(idTitolo)
            .setMessage(idMessaggio)
            .setCancelable(false)
            .setPositiveButton(R.string.bottone_ok) { _ , _ -> activity!!.onBackPressed() }
            .show()
        chiudiTastiera()
    }


    private fun chiudiTastiera() {
        val view = (activity as HomeActivity).currentFocus
        if (view != null) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private var popupAperto = false
    private fun mostraPopupErroreBackPremuto() {
        if(popupAperto) { return }

        popupAperto = true
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.popup_titolo_attenzione)
            .setMessage(R.string.tornando_indietro_i_valori_inseriti_saranno_persi_continuare)
            .setPositiveButton(R.string.bottone_conferma) { dialog, _ ->
                popupAperto = false
                activity!!.onBackPressed()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.bottone_annulla) { dialog, _ ->
                popupAperto = false
                dialog.dismiss()
            }
            .setOnCancelListener { popupAperto = false }
            .show()
        chiudiTastiera()
    }

}