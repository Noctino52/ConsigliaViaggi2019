package ingsw.server.dao

import ingsw.server.entity.Utente
import java.sql.PreparedStatement
import java.sql.SQLException
import kotlin.jvm.Throws

class UtenteDAOMySQL: UtenteDAO {
    override fun ottieniNomiUtenteCheContengonoString(nomeUtente: String): List<Utente> {
        if(nomeUtente.isEmpty()) {
            return listOf()
        }

        val querySql = "SELECT * FROM utente WHERE nomeUtente LIKE ?"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, "%$nomeUtente%")
        val resultSet = preparedStatement.executeQuery()

        return ConvertitoreResultSet.ottieniListaUtenteDaResultSet(resultSet)
    }


    override fun verificaNomeUtenteDisponibile(nomeUtente: String): Boolean {
        if(nomeUtente.length < 4 || nomeUtente.length > 16 || nomeUtente.contains(" ")) {
            return false
        }

        val querySql = "SELECT COUNT(id) as occupato FROM utente WHERE nomeUtente = ?"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, nomeUtente)
        val resultSet = preparedStatement.executeQuery()

        resultSet.next()
        return !resultSet.getBoolean("occupato")
    }

    override fun ottieniUtenteCompletoDaNomeUtente(nomeUtente: String): Utente? {
        val querySql = "SELECT * FROM utente WHERE nomeUtente LIKE ?"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, nomeUtente)
        val resultSet = preparedStatement.executeQuery()

        if(resultSet.next()) {
            val utente = ConvertitoreResultSet.ottieniUtenteCompletoDaResultSet(resultSet)
            return utente
        }
        return null
    }

    override fun verificaEmailDisponibile(email: String): Boolean {
        val querySql = "SELECT COUNT(id) as occupato FROM utente WHERE emailUtente = ?"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, email)
        val resultSet = preparedStatement.executeQuery()

        if(resultSet.next()) {
            return !resultSet.getBoolean("occupato")
        }
        return false
    }

    override fun salvaNuovoUtente(utente: Utente): Boolean {
        try {
            MySQLConnection.iniziaTransazione()

            inserisciUtente(utente)

            MySQLConnection.salvaTransazione()
            return true
        }
        catch (e: SQLException) {
            MySQLConnection.annullaTransazione()
            return false
        }
        finally {
            MySQLConnection.finisciTransazione()
        }
    }

    @Throws(SQLException::class)
    private fun inserisciUtente(utente: Utente) {
        val inserimentoUtenteSql = "INSERT INTO " +
                "utente(nomeUtente, nomeReale, cognomeReale, preferisceNomeReale, emailUtente, password) VALUES " +
                "(?, ?, ?, ?, ?, ?);"
        val utenteStatement = MySQLConnection.preparaQuery(inserimentoUtenteSql)
        riempiStatementInserimentoConUtente(utenteStatement, utente)
        utenteStatement.executeUpdate()
    }

    private fun riempiStatementInserimentoConUtente(statement: PreparedStatement, utente: Utente) {
        statement.setString(1, utente.nomeUtente)
        statement.setString(2, utente.nomeReale)
        statement.setString(3, utente.cognomeReale)
        if(utente.preferisceNomeReale != null) {
            statement.setBoolean(4, utente.preferisceNomeReale!!)
        }
        else {
            statement.setNull(4, java.sql.Types.BOOLEAN)
        }
        statement.setString(5, utente.emailUtente)
        statement.setString(6, utente.password)
    }
}