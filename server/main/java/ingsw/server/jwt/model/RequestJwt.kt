package ingsw.server.jwt.model

class RequestJwt {
    var nomeUtente: String? = null
    var password: String? = null

    //Costruttore default per il JSON
    constructor()

    constructor(username: String, password: String)
}