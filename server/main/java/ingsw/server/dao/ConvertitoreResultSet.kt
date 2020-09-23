package ingsw.server.dao

import ingsw.server.entity.Recensione
import java.sql.SQLException
import ingsw.server.entity.Struttura
import ingsw.server.entity.Utente
import java.sql.ResultSet

object ConvertitoreResultSet {
    fun ottieniListaRecensioniDaResultSet(resSet: ResultSet): List<Recensione> {
        val listaRecensioni = mutableListOf<Recensione>()
        while(resSet.next()) {
            val rec = ottieniRecensioneDaResultSet(resSet)
            rec.autore = ottieniUtenteRidottoDaResultSet(resSet)
            rec.struttura = ottieniStrutturaDaResultSet(resSet)
            listaRecensioni.add(rec)
        }
        return listaRecensioni
    }

    fun ottieniRecensioneDaResultSet(resSet: ResultSet): Recensione {
        return Recensione(resSet.getInt("valutazione"), resSet.getString("testo"),
                resSet.getDate("dataCreazione"), null, null)
    }


    fun ottieniListaUtenteDaResultSet(resSet: ResultSet): List<Utente> {
        val listaStrutture = mutableListOf<Utente>()
        while(resSet.next()) {
            listaStrutture.add(ottieniUtenteRidottoDaResultSet(resSet))
        }
        return listaStrutture
    }

    fun ottieniListaStruttureDaResultSet(rs: ResultSet): List<Struttura> {
        val listaStrutture = mutableListOf<Struttura>()
        while(rs.next()) {
            listaStrutture.add(ottieniStrutturaDaResultSet(rs))
        }
        return listaStrutture
    }


    fun ottieniUtenteCompletoDaResultSet(resSet: ResultSet): Utente {
        return Utente(resSet.getString("nomeUtente"),
                resSet.getString("password"),
                resSet.getString("nomeReale"),
                resSet.getString("cognomeReale"),
                resSet.getBoolean("preferisceNomeReale"),
                resSet.getString("emailUtente"))
    }

    private fun ottieniUtenteRidottoDaResultSet(resSet: ResultSet): Utente {
        return Utente(resSet.getString("nomeUtente"),
                "",
                resSet.getString("nomeReale"),
                resSet.getString("cognomeReale"),
                resSet.getBoolean("preferisceNomeReale"),
                resSet.getString("emailUtente"))
    }

    private fun ottieniStrutturaDaResultSet(resSet: ResultSet): Struttura {
        val media = try {
            resSet.getFloat("mediaRecensioni")
        }
        catch (e: SQLException) {
            null
        }

        return Struttura(resSet.getString("nomeStruttura"),
                resSet.getString("indirizzo"),
                resSet.getDouble("latitudine"),
                resSet.getDouble("longitudine"),
                resSet.getString("tipoStruttura"),
                resSet.getString("descrizione"),
                resSet.getString("emailStruttura"),
                resSet.getString("sitoWeb"),
                resSet.getString("numeroTelefono"),
                media)
    }
}