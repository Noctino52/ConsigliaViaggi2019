package ingsw.server.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import ingsw.server.dao.DAOFactory
import ingsw.server.entity.Recensione

@RestController
@RequestMapping("/recensione")
class RecensioniController {
    private val reviewDAO = DAOFactory.getRecensioneDAO()

    @RequestMapping(value = ["/aggiungiNuovaRecensione"], method = [RequestMethod.POST])
    fun aggiungiNuovaRecensione(@RequestBody review: Recensione): Boolean {
        println("CIAOOOO")
        val auth = SecurityContextHolder.getContext().authentication
        println(auth.toString())
        val nomeUtenteAutore = (auth.principal as User).username
        println("CIAOOOO")
        return reviewDAO.aggiungiNuovaRecensione(review, nomeUtenteAutore)
    }

    @RequestMapping("/ottieniRecensioneUtenteStruttura")
    fun ottieniRecensioneUtenteStruttura(@RequestParam("nomeUtente") nomeUtente: String,
                                         @RequestParam("nomeStruttura") nomeStruttura: String,
                                         @RequestParam("latitudine") latitudine: Double,
                                         @RequestParam("longitudine") longitudine: Double): Recensione? {
        println("CIAOOOO4")
        return reviewDAO.ottieniRecensioneUtenteStruttura(nomeUtente, nomeStruttura, latitudine, longitudine)
    }


    @RequestMapping("/ottieniRecensioniStruttura")
    fun ottieniRecensioniStruttura(@RequestParam("nomeStruttura") nomeStruttura: String,
                                   @RequestParam("latitudine") latitudine: Double,
                                   @RequestParam("longitudine") longitudine: Double): List<Recensione> {
        println("CIAOOOO3")
        return reviewDAO.ottieniRecensioniStruttura(nomeStruttura, latitudine, longitudine)
    }

    @RequestMapping("/ottieniRecensioniUtente")
    fun ottieniRecensioniUtente(@RequestParam("nomeUtente") nomeUtente: String): List<Recensione> {
        println("CIAOOOO2")
        return reviewDAO.ottieniRecensioniUtente(nomeUtente)
    }
}