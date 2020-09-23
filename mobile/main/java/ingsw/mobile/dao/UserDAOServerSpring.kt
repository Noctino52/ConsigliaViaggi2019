package ingsw.mobile.dao

import android.content.Context
import ingsw.mobile.R
import ingsw.mobile.entity.User
import ingsw.mobile.entity.ClientHttp
import ingsw.mobile.entity.JsonUtil
import okhttp3.HttpUrl
import java.util.Properties

class UserDAOServerSpring(context: Context): UserDAO {
    private val baseUrl : String

    init {
        val rawResource = context.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        baseUrl = "${props.getProperty("baseUrl")}/utente"
    }

    override fun AvaibleEmail(email: String): Boolean? {
        val endPoint = "$baseUrl/verificaEmailDisponibile"
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder()
            .addQueryParameter("email", email)
            .build()
        return ClientHttp.doGetRequest(httpUrl)?.toBoolean()
    }

    override fun AvaibleUsername(nomeUtente: String): Boolean? {
        val endPoint = "$baseUrl/verificaNomeUtenteDisponibile"
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder()
            .addQueryParameter("nomeUtente", nomeUtente)
            .build()
        return ClientHttp.doGetRequest(httpUrl)?.toBoolean()
    }


    override fun getUsernameThatHaveString(nomeUtente: String): List<User> {
        val endPoint = "${baseUrl}/ottieniNomiUtenteCheContengonoString"
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder()
            .addQueryParameter("nomeUtente", nomeUtente)
            .build()

        val json = ClientHttp.doGetRequest(httpUrl)
        return JsonUtil.fullListUserJSON(json)
    }
}
