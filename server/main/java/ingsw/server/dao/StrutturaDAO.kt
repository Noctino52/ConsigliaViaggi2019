package ingsw.server.dao
import ingsw.server.entity.Struttura
interface StrutturaDAO {
    fun ottieniTutteStrutture(): List<Struttura>
    fun ottieniStruttureCheContengonoString(nomeStruttura: String): List<Struttura>
}