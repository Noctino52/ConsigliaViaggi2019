package ingsw.mobile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.RecAdapter
import ingsw.mobile.controller.StructureController
import ingsw.mobile.databinding.FragmentStrutturaBinding
import ingsw.mobile.entity.Review
import ingsw.mobile.entity.Structure

class StructureFragment(private val structure: Structure) : Fragment() {
    private lateinit var binding: FragmentStrutturaBinding
    private lateinit var structureController: StructureController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as HomeActivity).abilitaRicerca(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_struttura, container, false)
        binding.apply {
            textViewNomeStruttura.text = structure.nomeStruttura
            textViewAvvisoRecensione.isVisible = false
            whatIfFull(textViewDescrizione, structure.descrizione)
            whatIfFull(textViewIndirizzo, structure.indirizzo)
            whatIfFull(textViewTelefono, structure.numeroTelefono)
            whatIfFull(textViewEmail, structure.emailStruttura)
            whatIfFull(textViewSitoWeb, structure.sitoWeb)
            buttonVisualizzaSuMappa.setOnClickListener {
                structureController.openMapWithStructure(structure)
            }
        }

        setButton()
        return  binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        structureController = StructureController(this, activity!!, structure)
        structureController.fillAsyncReview(structure)
    }


    override fun onResume() {
        super.onResume()
        structureController.fillSelectedStructure()
    }

    override fun onStart() {
        super.onStart()
        structureController.verifiyIfButtonWriteRecShouldBeOn()
    }

    private fun setButton() {
        binding.apply {
            buttonScriviRecensione.setOnClickListener { structureController.openStructureNewReview() }
            buttonFiltraRecensioni.setOnClickListener { structureController.openScreenFilter() }
        }
        disabilitaBottoneScrittura()
    }

    private fun whatIfFull(textView: TextView, testo: String?) {
        textView.text = if(testo.isNullOrBlank()) {
            activity!!.getString(R.string.campo_non_disponibile_struttura)
        }
        else {
            testo
        }
    }

    fun abilitaBottoneScrittura() {
        binding.buttonScriviRecensione.isEnabled = true
    }

    fun disabilitaBottoneScrittura() {
        binding.buttonScriviRecensione.isEnabled = false
    }

    fun impostaMessaggioErroreRecensione(idMessaggio: Int) {
        if(activity == null) { return }

        binding.textViewAvvisoRecensione.text = activity!!.getString(idMessaggio)
        binding.textViewAvvisoRecensione.isVisible = true
    }

    fun impostaValutazione(valutazione: Float) {
        binding.textViewValutazione.text = if(valutazione == 0f) {
             activity!!.getString(R.string.nessuna_valutazione_disponibile)
        }
        else {
            String.format("%.1f", valutazione)
        }
        impostaStelle(valutazione)
    }

    private fun impostaStelle(valutazione: Float) {
        binding.apply {
            imageStella1.setImageResource(ottieniStella(1, valutazione))
            imageStella2.setImageResource(ottieniStella(2, valutazione))
            imageStella3.setImageResource(ottieniStella(3, valutazione))
            imageStella4.setImageResource(ottieniStella(4, valutazione))
            imageStella5.setImageResource(ottieniStella(5, valutazione))
        }
    }

    fun ottieniStella(numeroStella: Int, valutazione: Float): Int {
        if(numeroStella < 1 || numeroStella > 5 || valutazione < 0 || valutazione > 5) {
            throw IllegalArgumentException()
        }

        return when {
            (numeroStella-valutazione < 0.3) -> R.drawable.ic_star_black_24dp
            (numeroStella-valutazione <= 0.75) -> R.drawable.ic_star_half_black_24dp
            else -> R.drawable.ic_star_border_black_24dp
        }
    }

    fun impostaListViewRecensioni(recensioni: List<Review>) {
        if(activity == null) { return }

        val stringaNumeroRecensioni = activity!!.getString(R.string.numero_recensioni_struttura)
        binding.textViewRecensioni.text = String.format(stringaNumeroRecensioni, recensioni.size)

        binding.listViewRecensioni.adapter =
            RecAdapter(
                activity!!,
                R.layout.recensione_row,
                recensioni
            )
    }
}