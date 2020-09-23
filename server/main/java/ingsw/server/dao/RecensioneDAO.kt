package ingsw.server.dao
import ingsw.server.entity.Recensione
interface RecensioneDAO {
    fun ottieniRecensioniStruttura(nomeStruttura: String, latitudine: Double, longitudine: Double): List<Recensione>
    fun aggiungiNuovaRecensione(recensione: Recensione, nomeAutore: String): Boolean
    fun ottieniRecensioneUtenteStruttura(nomeUtente: String, nomeStruttura: String, latitudine: Double, longitudine: Double): Recensione?
    fun ottieniRecensioniUtente(nomeUtente: String): List<Recensione>
}
