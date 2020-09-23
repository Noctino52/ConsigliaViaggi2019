package ingsw.mobile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.controller.SearchController
import ingsw.mobile.databinding.FragmentFiltriRicercaBinding
import ingsw.mobile.entity.SearchFilter

class FilterSearchFragment(private val filtro: SearchFilter,
                           private val searchController: SearchController) : Fragment() {
    private lateinit var binding: FragmentFiltriRicercaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as HomeActivity).abilitaRicerca(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filtri_ricerca,
            container, false)
        setGrafica()
        return binding.root
    }

    private fun setGrafica() {
        binding.buttonAnnullaFiltriRicerca.setOnClickListener { activity!!.onBackPressed() }
        binding.buttonConfermaFiltriRicerca.setOnClickListener { confirmedPressed() }

        setRadioGroup()
        setSpinnerDistanzaMassima()
        setSpinnerStelleMinime()
    }

    private fun setRadioGroup() {
        binding.apply {
            if (filtro.gpsON && filtro.sort == 3) {
                radioButtonRicercaDistanza.isChecked = true
            }
            else if (filtro.sort == 3 || filtro.sort == 1) {
                radioButtonRicercaStelleCrescenti.isChecked = true
            }
            else {
                radioButtonRicercaStelleDecrescenti.isChecked = true
            }

            if(!filtro.gpsON) {
                radioButtonRicercaDistanza.isEnabled = false
            }
        }
    }

    private fun setSpinnerStelleMinime() {
        binding.spinnerNumeroMinimoStelle.setSelection(filtro.starMin)
        binding.spinnerNumeroMinimoStelle.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                filtro.starMin = pos
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun setSpinnerDistanzaMassima() {
        if(!filtro.gpsON) {
            binding.spinnerDistanzaMassimaRicerca.isEnabled = false
        }
        else {
            binding.spinnerDistanzaMassimaRicerca.setSelection(when(filtro.maxKM) {
                1000f -> 1
                3000f -> 2
                5000f -> 3
                10000f -> 4
                else -> 0
            })
            binding.spinnerDistanzaMassimaRicerca.onItemSelectedListener = (object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                    filtro.maxKM = when(pos) {
                        1 -> 1000f
                        2 -> 3000f
                        3 -> 5000f
                        4 -> 10000f
                        else -> Float.MAX_VALUE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
        }
    }

    private fun confirmedPressed() {
        binding.apply {
            filtro.sort = when(radioGroup.checkedRadioButtonId) {
                radioButtonRicercaDistanza.id -> 3
                radioButtonRicercaStelleDecrescenti.id -> 2
                else -> 1
            }

            searchController.doSearch(filtro.query)
            activity!!.onBackPressed()
        }
    }
}