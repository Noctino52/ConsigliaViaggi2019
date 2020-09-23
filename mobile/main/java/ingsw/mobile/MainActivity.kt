package ingsw.mobile

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ingsw.mobile.controller.LoginController
import ingsw.mobile.R
import ingsw.mobile.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var loginController: LoginController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        loginController = LoginController()
        loginController.saveMainActivityStatic(this)
        loginController.alreadyConnected()
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            editTextUsername.setText(R.string.stringa_vuota)
            editTextUsername.clearFocus()
            editTextPasswordLogin.setText(R.string.stringa_vuota)
            editTextPasswordLogin.clearFocus()

            textViewRegistrati.setOnClickListener { loginController.openRegistration() }
            textViewAccediVisitatore.setOnClickListener {
                loginController.openHome(this@MainActivity)
            }

            buttonAccedi.setOnClickListener { accediPremuto() }
        }
    }

    private fun accediPremuto() {
        binding.apply {
            buttonAccedi.isEnabled = false
            val mappaEditText = mapOf<String,EditText>(
                Pair("nomeUtente", editTextUsername),
                Pair("password", editTextPasswordLogin)
            )
            loginController.doLogin(mappaEditText)
        }
    }

    fun attivaBottoneAccedi() {
        binding.buttonAccedi.isEnabled = true
    }

    fun mostraErrorEditText(editText: EditText, idMessaggio: Int, attivaFocus: Boolean) {
        editText.error = getString(idMessaggio)
        if (attivaFocus) {
            editText.requestFocus()
        }
    }

    fun mostraPopupErroreLogin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.popup_titolo_attenzione)
            .setMessage(R.string.nome_utente_o_password_errati)
            .setPositiveButton(R.string.bottone_ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}