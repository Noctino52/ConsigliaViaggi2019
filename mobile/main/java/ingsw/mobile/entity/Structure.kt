package ingsw.mobile.entity

import java.lang.IllegalArgumentException
import java.util.Objects
import java.util.regex.Pattern

class Structure(
    nomeStruttura: String,
    indirizzo: String,
    var latitudine: Double,
    var longitudine: Double,
    var tipoStruttura: String,
    descrizione: String? = null,
    emailStruttura: String? = null,
    sitoWeb: String? = null,
    numeroTelefono: String? = null,
    var mediaRecensioni: Float? = null,
    var distanzaDaUtente: Float? = null
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Structure

        if (latitudine != other.latitudine||
            longitudine != other.longitudine ||
            nomeStruttura != other.nomeStruttura) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(nomeStruttura.hashCode(), latitudine.hashCode(), longitudine.hashCode())
    }

    var nomeStruttura = nomeStruttura
        set(value) {
            if(value.length > 40) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var indirizzo = indirizzo
        set(value) {
            if(value.length > 100) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var emailStruttura = emailStruttura
        set(value) {
            if(value != null && emailNonValida(value)) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var descrizione = descrizione
        set(value) {
            if(value != null && value.length > 100) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var sitoWeb = sitoWeb
        set(value) {
            if(value != null && value.length > 2080) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var numeroTelefono = numeroTelefono
        set(value) {
            if(value != null && numeroNonValido(value)) {
                throw IllegalArgumentException()
            }
            field = value
        }

    private fun numeroNonValido(numero: String): Boolean {
        val regexNumeroValido = Regex("(^\\+[0-9]+)|(^[0-9]+)")
        return numero.length < 5 || numero.length > 15 || !regexNumeroValido.matches(numero)
    }

    private fun emailNonValida(email: String): Boolean {
        val emailPattern = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(emailPattern)

        val matcher = pattern.matcher(email)
        return !matcher.matches() || email.length > 320
    }

    constructor(nomeStruttura: String, latitudine: Double, longitudine: Double):
            this(nomeStruttura, "", latitudine, longitudine, "")
}
