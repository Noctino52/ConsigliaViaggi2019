package ingsw.mobile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ingsw.mobile.HomeActivity
import ingsw.mobile.R
import ingsw.mobile.controller.HomeController
import ingsw.mobile.databinding.FragmentHomeBaseBinding

class HomeFragment(private val homeController: HomeController): Fragment() {
    private lateinit var binding: FragmentHomeBaseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonApriMappa.isEnabled = true
            buttonApriMappa.setOnClickListener {
                buttonApriMappa.isEnabled = false
                homeController.openTheMap()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as HomeActivity).abilitaRicerca(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_base, container, false)
        binding.apply {
            buttonCercaHotel.setOnClickListener { homeController.doResearch("Hotel") }
            buttonCercaRistorante.setOnClickListener { homeController.doResearch("Ristorante") }
            buttonCercaAttrazioneTuristica.setOnClickListener {
                homeController.doResearch("AttrazioneTuristica") }
        }
        return binding.root
    }
}