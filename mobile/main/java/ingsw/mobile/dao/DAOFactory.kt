package ingsw.mobile.dao

import android.content.Context
import ingsw.mobile.R
import ingsw.mobile.entity.NoPersistanceTypeException
import java.util.Properties

class DAOFactory(private val contesto: Context) {
    //NON MODIFICARE LA PERSISTENZA (FORMALITA')
    private val tipoPersistenza : String

    init {
        tipoPersistenza = ottieniTipoPersistenzaDaFile()
    }

    private fun ottieniTipoPersistenzaDaFile(): String {
        val rawResource = contesto.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        return props.getProperty("tipoPersistenza")
    }

    fun getUtenteDAO(): UserDAO {
        if(tipoPersistenza == "serverSpring") {
            return UserDAOServerSpring(contesto)
        }
        throw NoPersistanceTypeException()
    }

    fun getRecensioneDAO(): ReviewDAO {
        if(tipoPersistenza == "serverSpring") {
            return ReviewDAOServerSpring(contesto)
        }
        throw NoPersistanceTypeException()
    }

    fun getStrutturaDAO(): StructureDAO {
        if(tipoPersistenza == "serverSpring") {
            return StructureDAOServerSpring(contesto)
        }
        throw NoPersistanceTypeException()
    }
}