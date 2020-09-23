package ingsw.mobile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.controller.StructureController
import ingsw.mobile.databinding.FragmentFiltriBinding

class FilterRecFragment(private val structureController: StructureController): Fragment() {
    private lateinit var binding: FragmentFiltriBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as HomeActivity).abilitaRicerca(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtri, container, false)
        binding.buttonAnnullaFiltri.setOnClickListener { activity!!.onBackPressed() }
        binding.buttonConfermaFiltri.setOnClickListener { confirmedPressed() }
        return binding.root
    }

    private fun confirmedPressed() {
        binding.apply {
            val ordinamentoScelto = when(radioGroup.checkedRadioButtonId) {
                radioButtonMenoRecente.id -> 2
                radioButtonPiuStelle.id -> 3
                radioButtonMenoStelle.id -> 4
                else -> 1
            }

            val newStars = arrayOf(true, true, true, true, true)
            if(!checkBoxUnaStella.isChecked) { newStars[0] = false }
            if(!checkBoxDueStelle.isChecked) { newStars[1] = false }
            if(!checkBoxTreStelle.isChecked) { newStars[2] = false }
            if(!checkBoxQuattroStelle.isChecked) { newStars[3] = false }
            if(!checkBoxCinqueStelle.isChecked) { newStars[4] = false }

            structureController.setFilter(ordinamentoScelto, newStars)
        }
        activity!!.onBackPressed()
    }
}