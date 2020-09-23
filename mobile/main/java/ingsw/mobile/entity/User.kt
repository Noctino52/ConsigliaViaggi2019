package ingsw.mobile.entity

import java.util.regex.Pattern

class User(
        nomeUtente: String,
        val password: String,
        nomeReale: String? = null,
        cognomeReale: String? = null,
        var preferisceNomeReale: Boolean? = null,
        emailUtente: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (nomeUtente != other.nomeUtente) return false

        return true
    }


    override fun hashCode(): Int {
        return nomeUtente.hashCode()
    }


    var nomeUtente = nomeUtente
        set(value) {
            if(value.length > 60) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var nomeReale = nomeReale
        set(value) {
            if(value != null && value.length > 60) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var cognomeReale = cognomeReale
        set(value) {
            if(value != null && value.length > 60) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var emailUtente = emailUtente
        set(value) {
            if(value != null && emailNonValida(value)) {
                throw IllegalArgumentException()
            }
            field = value
        }

    companion object{
        fun emailNonValida(email: String): Boolean {
            val emailPattern = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(emailPattern)

            val matcher = pattern.matcher(email)
            return !matcher.matches() || email.length > 320
        }
    }
}