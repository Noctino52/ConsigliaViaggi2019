package ingsw.server.dao
import ingsw.server.entity.Utente
interface UtenteDAO {
    fun ottieniNomiUtenteCheContengonoString(nomeUtente: String): List<Utente>
    fun verificaEmailDisponibile(email: String): Boolean
    fun ottieniUtenteCompletoDaNomeUtente(nomeUtente: String): Utente?
    fun salvaNuovoUtente(utente: Utente): Boolean
    fun verificaNomeUtenteDisponibile(nomeUtente: String): Boolean
}