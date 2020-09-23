package ingsw.server.controller

import ingsw.server.dao.DAOFactory
import ingsw.server.entity.Utente
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import ingsw.server.jwt.util.JwtTokenUtil
import ingsw.server.jwt.model.RequestJwt
import ingsw.server.jwt.model.ResponseJwt
import ingsw.server.jwt.service.UtenteDetailsService
import kotlin.jvm.Throws

@RestController
class JwtAuthenticationController {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil
    @Autowired
    private lateinit var utenteDetailsService: UtenteDetailsService


    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody requestJwt: RequestJwt): ResponseEntity<*> {
        println("authenticationRequest: " +requestJwt.nomeUtente +requestJwt.password)
        return if (richiestaAutenticazioneNonValida(requestJwt)) {
            println("Oh no!")
            ResponseEntity.badRequest().build<Any>()
        }
        else {
            val token = effettuaAutenticazioneEdOttieniToken(requestJwt)
            ResponseEntity.ok(ResponseJwt(token))
        }
    }
    @RequestMapping(value = ["/registrazione"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun registrazioneUtente(@RequestBody utente_Utente: Utente): Boolean {
        val utenteDAO = DAOFactory.getUtenteDAO()
        val occupato = !utenteDAO.verificaNomeUtenteDisponibile(utente_Utente.nomeUtente)
        if(occupato) { return false }
        
        return utenteDetailsService.criptaPasswordESalvaUtente(utente_Utente)
    }
    private fun effettuaAutenticazione(userName: String, password: String) {
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userName, password)
        println("cacca:"+usernamePasswordAuthenticationToken)
        authenticationManager.authenticate(usernamePasswordAuthenticationToken)
    }
    private fun effettuaAutenticazioneEdOttieniToken(requestJwt: RequestJwt): String {
        val nomeUtente = requestJwt.nomeUtente!!.toLowerCase()
        val password = requestJwt.password!!
        effettuaAutenticazione(nomeUtente, password)

        val userDetails = utenteDetailsService.loadUserByUsername(nomeUtente)
        println("userDetails: "+userDetails.username.toString() +userDetails.password.toString())
        return jwtTokenUtil.generaToken(userDetails)
    }
    private fun richiestaAutenticazioneNonValida(requestJwt: RequestJwt): Boolean {
        return requestJwt.nomeUtente == null || requestJwt.password == null
    }

}