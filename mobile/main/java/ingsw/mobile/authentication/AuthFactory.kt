package ingsw.mobile.authentication

import android.content.Context
import ingsw.mobile.R
import ingsw.mobile.entity.NoValidAuthenticationException
import java.util.Properties

class AuthFactory (private val context: Context){
    private val tipoAutenticazione: String
    init {
        tipoAutenticazione = obtainAuthFromFile()
    }

    fun getAuthProvider(): AuthProvider {
        //NON MODIFICARE TIPOAUTENTICAZIONE(FORMALITA')
        if(tipoAutenticazione == "jwt") {
            return JwtAuth(context)
        }
        throw NoValidAuthenticationException()
    }

    private fun obtainAuthFromFile(): String {
        val rawResource = context.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        return props.getProperty("tipoAutenticazione")
    }
}