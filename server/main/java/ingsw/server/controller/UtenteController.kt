package ingsw.server.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ingsw.server.dao.DAOFactory
import ingsw.server.entity.Utente

@RestController
@RequestMapping("/utente")
class UtenteController {
    private val userDAO = DAOFactory.getUtenteDAO()

    @RequestMapping("/ottieniNomiUtenteCheContengonoString")
    fun ottieniNomiUtenteCheContengonoString(@RequestParam("nomeUtente") userName: String): List<Utente> {
        println("CIAOOOO6")
        return userDAO.ottieniNomiUtenteCheContengonoString(userName)
    }

    @RequestMapping("/verificaEmailDisponibile")
    fun verificaEmailDisponibile(@RequestParam("email") Email: String): Boolean {
        return userDAO.verificaEmailDisponibile(Email)
    }

    @RequestMapping("/verificaNomeUtenteDisponibile")
    fun verificaNomeUtenteDisponibile(@RequestParam("nomeUtente") userName: String): Boolean {
        return userDAO.verificaNomeUtenteDisponibile(userName)
    }
}
