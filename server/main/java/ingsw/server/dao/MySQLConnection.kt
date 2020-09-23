package ingsw.server.dao

import java.sql.DriverManager
import java.sql.PreparedStatement
import ingsw.server.Application
import java.sql.Connection
import java.util.Properties

object MySQLConnection {
    private val prop = Properties()
    private var connessione: Connection

    init {
        val fileStream = Application::class.java.classLoader.getResourceAsStream("databaseInformation.properties")
        prop.load(fileStream)
        connessione = ottieniConnessioneAlDatabase()
    }

    fun preparaQuery(query: String): PreparedStatement {
        aggiornaConnessioneAlDatabase()
        return connessione.prepareStatement(query)
    }


    fun salvaTransazione() {
        connessione.commit()
    }

    fun finisciTransazione() {
        connessione.autoCommit = true
    }

    fun iniziaTransazione() {
        connessione.autoCommit = false
    }

    fun annullaTransazione() {
        connessione.rollback()
    }

    private fun aggiornaConnessioneAlDatabase() {
        val tempoDiAttesa = 1000
        if(!connessione.isValid(tempoDiAttesa)) {
            connessione = ottieniConnessioneAlDatabase()
        }
    }


    private fun ottieniConnessioneAlDatabase(): Connection {
        return try {
            val username = prop.getProperty("username")
            val password = prop.getProperty("password")
            val databaseName = prop.getProperty("database")
            val endPoint = prop.getProperty("endPoint")
            val port = prop.getProperty("port")

            val jdbcUrl = "jdbc:mysql://$endPoint:$port/$databaseName?user=$username&password=$password&autoReconnect=true&useSSL=false"
            DriverManager.getConnection(jdbcUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e.cause)
        }
    }
}