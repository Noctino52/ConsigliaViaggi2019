package ingsw.mobile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.StructAdapter
import ingsw.mobile.controller.SearchController
import ingsw.mobile.databinding.FragmentRicercaBinding
import ingsw.mobile.entity.Structure

class SearchFragment(private val query: String,
                     private val homeActivity: HomeActivity
): Fragment() {
    private lateinit var searchController: SearchController
    private lateinit var binding: FragmentRicercaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as HomeActivity).abilitaRicerca(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ricerca, container, false)
        binding.buttonFiltriRicerca.setOnClickListener { searchController.openFilter(query) }
        binding.listViewStrutture.requestFocus()
        searchController.getStructure(query)

        homeActivity.rimuoviFocusDaSearchBar()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        homeActivity.nascondiSearchBar()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchController = SearchController(homeActivity, this)
    }


    fun setListViewStrutture(listaStrutture: List<Structure>) {
        if(activity!= null) {
            val adapter = StructAdapter(
                activity!!,
                R.layout.strutture_row,
                listaStrutture
            )
            binding.listViewStrutture.adapter = adapter
            binding.listViewStrutture.emptyView = binding.textViewListaVuota
            binding.listViewStrutture.setOnItemClickListener { _, _, position, _ ->
                val struttura = adapter.getItem(position)!!
                searchController.openStructure(struttura)
            }
        }
    }

    fun setTextViewRisultatiRicerca(query: String) {
        binding.textViewRisultatiRicerca.text = query
    }

}