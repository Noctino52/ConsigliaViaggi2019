package ingsw.server.dao

import ingsw.server.entity.Struttura

class StrutturaDAOMySQL: StrutturaDAO {

    override fun ottieniTutteStrutture(): List<Struttura> {
        val querySql = "SELECT s.*, AVG(r.valutazione) as mediaRecensioni " +
                       "FROM struttura s LEFT JOIN recensione r ON s.id = r.idStruttura " +
                       "GROUP BY s.id"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        val resultSet = preparedStatement.executeQuery()
        return ConvertitoreResultSet.ottieniListaStruttureDaResultSet(resultSet)
    }

    override fun ottieniStruttureCheContengonoString(nomeStruttura: String): List<Struttura> {
        if(nomeStruttura.isEmpty()) {
            return listOf()
        }

        val querySql = "SELECT * FROM struttura WHERE nomeStruttura LIKE ?"
        val preparedStatement = MySQLConnection.preparaQuery(querySql)
        preparedStatement.setString(1, "%$nomeStruttura%")
        val resultSet = preparedStatement.executeQuery()

        return ConvertitoreResultSet.ottieniListaStruttureDaResultSet(resultSet)
    }
}