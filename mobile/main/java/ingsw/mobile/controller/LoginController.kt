package ingsw.mobile.controller

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.widget.EditText
import ingsw.mobile.HomeActivity
import ingsw.mobile.MainActivity
import ingsw.mobile.RegistrazioneActivity
import ingsw.mobile.authentication.AuthFactory
import ingsw.mobile.authentication.AuthProvider
import ingsw.mobile.dao.DAOFactory
import ingsw.mobile.dao.UserDAO
import ingsw.mobile.entity.ClientHttp
import ingsw.mobile.entity.User
import ingsw.mobile.R
import com.google.gson.GsonBuilder
import java.io.Serializable
import java.util.*

class LoginController: Serializable {
    companion object{
        private lateinit var main: MainActivity
        private lateinit var userDAO: UserDAO
        lateinit var registrazione: RegistrazioneActivity
        private lateinit var autenticatore: AuthProvider
        private lateinit var daoFactory: DAOFactory
        private var checkBox: Boolean = false
        lateinit var mappaCampi: Map<String, EditText>
        private lateinit var authFactory: AuthFactory
    }

    fun saveMainActivityStatic(mainActivity: MainActivity) {
        main = mainActivity
        authFactory = AuthFactory(mainActivity)
        autenticatore = authFactory.getAuthProvider()
        daoFactory = DAOFactory(mainActivity)
        userDAO = daoFactory.getUtenteDAO()
        ClientHttp.context = mainActivity
    }

    fun alreadyConnected() {
        val nomeUtenteAttuale = autenticatore.obtainUsername()
        if(!nomeUtenteAttuale.isNullOrBlank()) {
            openHome(main)
        }
    }

    fun setRegistazioneActivity(reg: RegistrazioneActivity) {
        registrazione = reg
    }

    fun doLogin(mappa: Map<String, EditText>) {
        mappaCampi = mappa
        if(recordNotNull(main)) {
            authentication(ottieniStringa(mappaCampi.getValue("nomeUtente")),
                        ottieniStringa(mappaCampi.getValue("password")), main)
        }
        else {
            main.attivaBottoneAccedi()
        }
    }

    fun authentication(nomeUtente: String, password: String, activity: Activity) {
        class Authenticator: AsyncTask<Any, Any, Boolean>(){
            override fun doInBackground(vararg params: Any?): Boolean {
               return autenticatore.doAccess(nomeUtente, password)
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                if (result == null || !result){
                    (activity as MainActivity).mostraPopupErroreLogin()
                }
                else {
                    openHome(activity)
                }
                if(activity is MainActivity){
                    activity.attivaBottoneAccedi()
                }
            }
        }

        Authenticator().execute()
    }

    fun openHome(activity: Activity) {
        val i = Intent(activity, HomeActivity::class.java)
        i.putExtra("controller",this)
        activity.startActivity(i)
        activity.finish()
        main.finish()
    }

    fun openRegistration() {
        val i = Intent(main, RegistrazioneActivity::class.java)
        i.putExtra("controller",this)
        main.startActivity(i)
    }

    fun doRegistration(mappa: Map<String, EditText>, check: Boolean) {
        mappaCampi = mappa
        checkBox = check
        checkRecordRegistration()
    }

    fun backWhenItRegister(listaCampi: List<EditText>) {
        if (atLeastRecordNotNull(listaCampi)) {
            registrazione.mostraPopupErroreBackPremuto()
        }
        else {
            registrazione.finish()
        }
    }

    private fun registration() {
        val email = ottieniStringa(mappaCampi.getValue("email"))
        val password = ottieniStringa(mappaCampi.getValue("password"))
        val nomeUtente = ottieniStringa(mappaCampi.getValue("nomeUtente"))
        val nome = ottieniStringa(mappaCampi.getValue("nomeReale"))
        val cognome = ottieniStringa(mappaCampi.getValue("cognomeReale"))

        val utente = User(nomeUtente, password, nome, cognome, checkBox, email)

        val jsonConverter = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
        val utenteJson = jsonConverter.toJson(utente, User::class.java)
        val urlBase = getUrlBaseFromFileConfig()

        class RichiestaHttpAsincrona: AsyncTask<Any, Any, Boolean>() {
            override fun doInBackground(vararg params: Any?): Boolean? {
                return try{
                    ClientHttp.doPostWithBodyRequestByJSON("$urlBase/registrazione",
                        utenteJson,false)?.toBoolean()
                }
                catch (e: IllegalArgumentException) {
                    return false
                }
            }

            override fun onPostExecute(result: Boolean?) {
                if(result == null || !result) {
                    registrazione.mostraPopupErroreRegistrazione()
                }
                else {
                    registrazione.mostraPopupRegistrazioneCompletata(nomeUtente, password)
                }
                registrazione.attivaBottoneRegistrazione()
            }
        }
        RichiestaHttpAsincrona().execute()
    }

    private fun uniquenessEmail() {
        class ControllaUnicitaEmail: AsyncTask<Any, Any, Boolean>(){
            override fun doInBackground(vararg params: Any?): Boolean? {
                return userDAO.AvaibleEmail(ottieniStringa(
                    mappaCampi.getValue("email")))
            }

            override fun onPostExecute(result: Boolean?) {
                if(result == null || result == false) {
                    registrazione.mostraErrorEditText(mappaCampi.getValue("email"),
                        R.string.email_gia_in_uso, true)
                    registrazione.attivaBottoneRegistrazione()
                }
                else {
                    uniquenessUser()
                }
            }
        }

        ControllaUnicitaEmail().execute()
    }

    private fun uniquenessUser(){
        class ControllaUnicitaUtente: AsyncTask<Any, Any, Boolean>(){
            override fun doInBackground(vararg params: Any?): Boolean? {
                return userDAO.AvaibleUsername(ottieniStringa(
                    mappaCampi.getValue("nomeUtente")))
            }

            override fun onPostExecute(result: Boolean?) {
                if(result == null || result == false) {
                    registrazione.mostraErrorEditText(mappaCampi.getValue("nomeUtente"),
                        R.string.nome_utente_gia_in_uso, true)
                    registrazione.attivaBottoneRegistrazione()
                }
                else {
                    registration()
                }
            }
        }

        ControllaUnicitaUtente().execute()
    }
    private fun checkWhiteSpaceUser(): Boolean {
        if (ottieniStringa(mappaCampi.getValue("nomeUtente")).contains(" ")) {
            registrazione.mostraErrorEditText(mappaCampi.getValue("nomeUtente"),
                R.string.il_nome_utente_non_puo_contenere_spazi, true)
            return false
        }
        return true
    }

    private fun minLenghtUsername(): Boolean {
        if (ottieniStringa(mappaCampi.getValue("nomeUtente")).length < 4) {
            registrazione.mostraErrorEditText(mappaCampi.getValue("nomeUtente"),
                R.string.l_username_deve_essere_lungo_4_caratteri, true)
            return false
        }
        return true
    }

    private fun sintaxEmail(): Boolean {
        if(User.emailNonValida(ottieniStringa(mappaCampi.getValue("email")))) {
            registrazione.mostraErrorEditText(mappaCampi.getValue("email"),
                R.string.email_non_valida, true)
            return false
        }
        return true
    }



    private fun checkRecordRegistration() {
        if (recordNotNull(registrazione) &&
            sintaxEmail() &&
            minLenghtPassword() &&
            checkEqualsPassword() &&
            minLenghtUsername() &&
            checkWhiteSpaceUser())
        {
            uniquenessEmail()
        }
        else {
            registrazione.attivaBottoneRegistrazione()
        }
    }


    private fun minLenghtPassword(): Boolean {
        if (ottieniStringa(mappaCampi.getValue("password")).length < 8) {
            registrazione.mostraErrorEditText(mappaCampi.getValue("password"),
                R.string.la_password_deve_contenere_8_caratteri, true)
            return false
        }
        else if (ottieniStringa(mappaCampi.getValue("confermaPassword")).length < 8) {
            registrazione.mostraErrorEditText(mappaCampi.getValue("confermaPassword"),
                R.string.la_password_deve_contenere_8_caratteri, true)
            return false
        }
        return true
    }
    private fun getUrlBaseFromFileConfig(): String {
        val rawResource = registrazione.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        return props.getProperty("baseUrl")
    }


    private fun recordNotNull(activity: Activity): Boolean {
        var campiVuoti = true
        for (editText in mappaCampi.values) {
            if (ottieniStringa(editText).isEmpty()) {
                if(activity is MainActivity) {
                    activity.mostraErrorEditText(editText,
                        R.string.il_campo_non_puo_essere_vuoto, false)
                }
                else if (activity is RegistrazioneActivity){
                    activity.mostraErrorEditText(editText,
                        R.string.il_campo_non_puo_essere_vuoto, false)
                }

                campiVuoti = false
            }
        }
        return campiVuoti
    }


    private fun checkEqualsPassword(): Boolean {
        if (ottieniStringa(mappaCampi.getValue("password")) !=
            ottieniStringa(mappaCampi.getValue("confermaPassword"))) {
            registrazione.mostraPopupErrorePasswordDiverse()
            return false
        }
        return true
    }

    private fun ottieniStringa(editText: EditText): String {
        return editText.text.toString().trim().replace(" +".toRegex(), " ")
    }


    private fun atLeastRecordNotNull(listEditText: List<EditText>): Boolean {
        for (editText in listEditText) {
            if (ottieniStringa(editText).isNotEmpty()) {
                return true
            }
        }
        return false
    }
}