package ingsw.server.dao

import ingsw.server.entity.Recensione
import ingsw.server.entity.Struttura
import java.sql.PreparedStatement
import java.sql.SQLException

class RecensioneDAOMySQL: RecensioneDAO {

    override fun ottieniRecensioniUtente(nomeUtente: String): List<Recensione> {
        if(nomeUtente.isEmpty()) {
            return listOf()
        }

        val querySql = "SELECT valutazione, testo, u.*, s.*" +
                "FROM recensione r JOIN utente u on r.idUtente = u.id\n" +
                "JOIN struttura s on r.idStruttura = s.id\n" +
                "WHERE r.idUtente = (SELECT id FROM utente WHERE nomeUtente = ?)"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, nomeUtente)
        val resultSet = preparedStatement.executeQuery()

        return ConvertitoreResultSet.ottieniListaRecensioniDaResultSet(resultSet)
    }

    override fun ottieniRecensioniStruttura(nomeStruttura: String,
                                            latitudine: Double, longitudine: Double): List<Recensione> {
        val querySql = "SELECT r.*, u.*, s.*" +
                "FROM recensione r JOIN utente u on r.idUtente = u.id\n" +
                "JOIN struttura s on r.idStruttura = s.id\n" +
                "WHERE r.idStruttura = (SELECT id FROM struttura WHERE nomeStruttura = ?" +
                "AND latitudine = ? AND longitudine = ?)"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, nomeStruttura)
        preparedStatement.setDouble(2, latitudine)
        preparedStatement.setDouble(3, longitudine)
        val resultSet = preparedStatement.executeQuery()

        return ConvertitoreResultSet.ottieniListaRecensioniDaResultSet(resultSet)
    }

    override fun aggiungiNuovaRecensione(recensione: Recensione, nomeAutore: String): Boolean {
        if(recensione.struttura == null) {
            return false
        }

        val querySql = "INSERT INTO recensione(valutazione, testo, idUtente, idStruttura) VALUES " +
                "(?, ?, (SELECT id FROM utente WHERE nomeUtente = ?), " +
                "(SELECT id FROM struttura WHERE nomeStruttura = ? " +
                "AND latitudine = ? AND longitudine = ?));"

        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setInt(1, recensione.valutazione)
        preparedStatement.setString(2, recensione.testo)
        preparedStatement.setString(3, nomeAutore)
        impostaStrutturaInStatement(4, preparedStatement, recensione.struttura!!)
        println("Albicocca: $preparedStatement")

        return eseguiStatement(preparedStatement)
    }

    override fun ottieniRecensioneUtenteStruttura(nomeUtente: String,
                                                  nomeStruttura: String,
                                                  latitudine: Double, longitudine: Double): Recensione? {
        val querySql = ("SELECT * FROM recensione r "
                + "WHERE r.idUtente = (SELECT id FROM utente WHERE nomeUtente = ?) "
                + "AND r.idStruttura = (SELECT id FROM struttura WHERE nomeStruttura = ? " +
                "AND latitudine = ? AND longitudine = ?)")

        val statement = MySQLConnection.preparaQuery(querySql)
        statement.setString(1, nomeUtente)
        statement.setString(2, nomeStruttura)
        statement.setDouble(3, latitudine)
        statement.setDouble(4, longitudine)

        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            return ConvertitoreResultSet.ottieniRecensioneDaResultSet(resultSet)
        }
        return null
    }



    private fun eseguiStatement(statement: PreparedStatement): Boolean {
        return try {
            statement.execute()
            true
        }
        catch (e: SQLException) {
            false
        }
    }

    private fun impostaStrutturaInStatement(indiceDiPartenza: Int,
                                            statement: PreparedStatement, struttura: Struttura) {
        var n = indiceDiPartenza
        statement.setString(n++, struttura.nomeStruttura)
        statement.setDouble(n++, struttura.latitudine)
        statement.setDouble(n, struttura.longitudine)
    }
}