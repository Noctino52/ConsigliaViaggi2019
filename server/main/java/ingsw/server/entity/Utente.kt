package ingsw.server.entity

data class Utente(
        var nomeUtente: String,
        //
        var password: String? = null,
        var nomeReale: String? = null,
        var cognomeReale: String? = null,
        var preferisceNomeReale: Boolean? = null,
        var emailUtente: String? = null
)