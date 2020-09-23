package ingsw.mobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ingsw.mobile.controller.LoginController
import ingsw.mobile.R
import ingsw.mobile.databinding.ActivityRegistrazioneBinding

class RegistrazioneActivity: AppCompatActivity() {
    private lateinit var loginController: LoginController
    private lateinit var binding: ActivityRegistrazioneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione)
        loginController = intent.getSerializableExtra("controller") as LoginController
        loginController.setRegistazioneActivity(this)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_registrazione
        )
        binding.buttonAnnullaRegistrazione.setOnClickListener { annullaRegistrazionePremuto() }
        binding.buttonRegistrati.setOnClickListener { registrazionePremuto() }
    }

    private fun registrazionePremuto() {
        binding.apply {
            buttonRegistrati.isEnabled = false
            val mappaEditText = mapOf<String,EditText>(
                Pair("email", editTextEmail),
                Pair("password", editTextPassword),
                Pair("confermaPassword", editTextConfermaPassword),
                Pair("nomeUtente", editTextNomeUtente),
                Pair("nomeReale", editTextNomeReale),
                Pair("cognomeReale", editTextCognomeReale)
            )

            loginController.doRegistration(mappaEditText, checkBoxNomeReale.isChecked)
        }
    }

    fun attivaBottoneRegistrazione() {
        val bottone = findViewById<Button>(R.id.buttonRegistrati)
        bottone.isEnabled = true
    }

    override fun onBackPressed() {
        binding.apply {
            val listaEditText = listOf<EditText>(editTextEmail, editTextPassword,
                editTextConfermaPassword, editTextNomeUtente,
                editTextNomeReale, editTextCognomeReale)
            loginController.backWhenItRegister(listaEditText)
        }
    }

    private fun annullaRegistrazionePremuto() {
        onBackPressed()
    }

    fun mostraErrorEditText(editText: EditText, idMessaggio: Int, attivaFocus: Boolean) {
        editText.error = getString(idMessaggio)
        if (attivaFocus) {
            editText.requestFocus()
        }
    }

    fun mostraPopupErroreRegistrazione() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.popup_titolo_errore)
            .setMessage(R.string.errore_durante_registrazione)
            .setPositiveButton(R.string.bottone_ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun mostraPopupRegistrazioneCompletata(nomeUtente: String, password: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.registrazione_avvenuta_con_successo)
            .setCancelable(false)
            .setPositiveButton(R.string.bottone_ok) { _, _ ->
                loginController.authentication(nomeUtente,password, this) }
            .show()
    }

    fun mostraPopupErroreBackPremuto() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.popup_titolo_attenzione)
            .setMessage(R.string.tornando_indietro_i_valori_inseriti_saranno_persi_continuare)
            .setPositiveButton(R.string.bottone_conferma) { _, _ -> finish() }
            .setNegativeButton(R.string.bottone_annulla) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun mostraPopupErrorePasswordDiverse() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.popup_titolo_attenzione)
            .setMessage(R.string.password_non_corrispondenti)
            .setPositiveButton(R.string.bottone_ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
