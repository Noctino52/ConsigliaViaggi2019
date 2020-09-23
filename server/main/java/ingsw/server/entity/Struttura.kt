package ingsw.server.entity
data class Struttura(
    var nomeStruttura: String,
    var indirizzo: String,
    var latitudine: Double,
    var longitudine: Double,
    //
    var tipoStruttura: String,
    var descrizione: String? = null,
    var emailStruttura: String? = null,
    var sitoWeb: String? = null,
    //
    var numeroTelefono: String? = null,
    var mediaRecensioni: Float? = null
)