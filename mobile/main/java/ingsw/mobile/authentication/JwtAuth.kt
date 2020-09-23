package ingsw.mobile.authentication

import android.content.Context
import ingsw.mobile.R
import ingsw.mobile.entity.ClientHttp
import okhttp3.Request
import org.json.JSONObject
import java.util.Properties
import java.util.Locale

class JwtAuth(context: Context): AuthProvider {
    private val baseUrl : String
    companion object{
        private val jwtToken = JwtToken()
        private var nomeUtente: String? = null
            set(value) {
                field = value?.toLowerCase(Locale.getDefault())
            }
    }


    override fun doAccess(nomeUtente: String, password: String): Boolean {
        val endPoint = "${baseUrl}/login"
        val richiestaLogin = "{\"nomeUtente\":\"$nomeUtente\", \"password\":\"$password\"}"

        val jsonResult = ClientHttp.doPostWithBodyRequestByJSON(endPoint, richiestaLogin, conAutenticazione = false)
            ?: return false

        val json = JSONObject(jsonResult)
        jwtToken.token = json.getString("token")
        println("token: ${jwtToken.token}")
        if(jwtToken.token == null || jwtToken.token!!.isEmpty()) {
            return false
        }
        JwtAuth.nomeUtente = nomeUtente
        return true
    }

    override fun addAuthRequestHttp(request: Request.Builder) {
        request.addHeader("Authorization", "Bearer ${jwtToken.token}")
    }

    override fun obtainUsername(): String? {
        return nomeUtente
    }
    init {
        baseUrl = obtainBaseUrlFromFile(context)
    }

    private fun obtainBaseUrlFromFile(context: Context): String {
        val rawResource = context.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        return props.getProperty("baseUrl")
    }


    override fun actuallyAuth(): Boolean {
        return !jwtToken.token.isNullOrBlank()
    }

    override fun doLogout() {
        nomeUtente = null
        jwtToken.token = null
    }
}