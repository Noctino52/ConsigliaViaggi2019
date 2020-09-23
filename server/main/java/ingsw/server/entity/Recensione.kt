package ingsw.server.entity
import java.util.Date
data class Recensione(
        var valutazione: Int,
        //
        var testo: String,
        var dataCreazione: Date,
        //
        var autore: Utente? = null,
        var struttura: Struttura? = null
)