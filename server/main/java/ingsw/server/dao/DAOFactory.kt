package ingsw.server.dao

import ingsw.server.entity.InvalidPersistanceException
import java.util.Properties

object DAOFactory {
    private val props = Properties()
    private val tipoPersistenza : String

    init {
        val fileInputStream = DAOFactory::class.java.classLoader.getResourceAsStream("config.properties")
        props.load(fileInputStream)
        tipoPersistenza = props.getProperty("tipoPersistenza")
    }


    fun getRecensioneDAO(): RecensioneDAO {
        if(tipoPersistenza == "mysql") {
            return RecensioneDAOMySQL()
        }
        throw InvalidPersistanceException()
    }

    fun getStrutturaDAO(): StrutturaDAO {
        if(tipoPersistenza == "mysql") {
            return StrutturaDAOMySQL()
        }
        throw InvalidPersistanceException()
    }

    fun getUtenteDAO(): UtenteDAO {
        if(tipoPersistenza == "mysql") {
            return UtenteDAOMySQL()
        }
        throw InvalidPersistanceException()
    }
}