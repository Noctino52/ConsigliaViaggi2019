package ingsw.mobile.controller

import android.content.Intent
import ingsw.mobile.MainActivity
import ingsw.mobile.R
import ingsw.mobile.authentication.AuthProvider
import ingsw.mobile.fragment.HomeFragment
import ingsw.mobile.fragment.MapFragment
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ingsw.mobile.HomeActivity
import ingsw.mobile.fragment.SearchFragment

class HomeController(private val homeActivity: HomeActivity,
                     private val authProvider: AuthProvider
): MenuItem.OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(authProvider.actuallyAuth()) {
            authProvider.doLogout()
            backToMainActivityWithClearStack()
        }
        else {
            backToMainActivity()
        }
        return true
    }


    fun doResearch(query: String){
        homeActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, SearchFragment(query, homeActivity),"ricerca")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .addToBackStack(null).commit()
    }

    private fun backToMainActivity() {
        val i = Intent(homeActivity, MainActivity::class.java)
        homeActivity.startActivity(i)
    }
    fun backFromHome() {
        homeActivity.supportFragmentManager
            .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        homeActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, HomeFragment(this))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit()
    }

    fun openTheMap() {
        homeActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, MapFragment(MapController(homeActivity)))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .addToBackStack(null).commit()
    }


    private fun backToMainActivityWithClearStack() {
        val i = Intent(homeActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.putExtra("EXIT", true)
        homeActivity.startActivity(i)
        homeActivity.finish()
    }
}