import ingsw.server.dao.UtenteDAOMySQL
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtenteDAOMySQLTest {
    private lateinit var utenteDAO: UtenteDAOMySQL

    @Before
    fun istanziaUtente() {
        utenteDAO = UtenteDAOMySQL()
    }

    @Test
    fun nomeUtenteNonDisponibileConLunghezzaMinoreDiQuattro() {
        assertFalse(utenteDAO.verificaNomeUtenteDisponibile("abc"))
    }

    @Test
    fun nomeUtenteNonDisponibileConLunghezzaMaggioreDiSedici() {
        assertFalse(utenteDAO.verificaNomeUtenteDisponibile("0123456789abcdefh"))
    }

    @Test
    fun nomeUtenteNonDisponibileConSpazi() {
        assertFalse(utenteDAO.verificaNomeUtenteDisponibile("ciao mondo"))
    }

    @Test
    fun nomeUtenteNonDisponibileInMinuscoloNelDatabase() {
        assertFalse(utenteDAO.verificaNomeUtenteDisponibile("Noctino52"))
    }

    @Test
    fun nomeUtenteNonDisponibileInMaiuscoloNelDatabase() {
        assertFalse(utenteDAO.verificaNomeUtenteDisponibile("NOCTINO52"))
    }

    @Test
    fun nomeUtenteDisponibileNelDatabase() {
        assertTrue(utenteDAO.verificaNomeUtenteDisponibile("test4321"))
    }

}