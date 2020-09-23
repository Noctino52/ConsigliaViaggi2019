package ingsw.server.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ingsw.server.dao.DAOFactory
import ingsw.server.entity.Struttura

@RestController
@RequestMapping("/struttura")
class StruttureController {
    private val structureDAO = DAOFactory.getStrutturaDAO()


    @RequestMapping("/ottieniTutteStrutture")
    fun ottieniTutteStrutture(): List<Struttura> {
        return structureDAO.ottieniTutteStrutture()
    }

    @RequestMapping("/ottieniStruttureCheContengonoString")
    fun ottieniStruttureCheContengonoString(@RequestParam("nomeStruttura") structureName: String): List<Struttura> {
        return structureDAO.ottieniStruttureCheContengonoString(structureName)
    }
}