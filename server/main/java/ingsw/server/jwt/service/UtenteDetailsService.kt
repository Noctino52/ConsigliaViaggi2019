package ingsw.server.jwt.service

import ingsw.server.dao.DAOFactory
import ingsw.server.jwt.util.AuthoritiesUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UtenteDetailsService: UserDetailsService {
    @Autowired
    private lateinit var bcryptEncoder: PasswordEncoder

    private var userDAO = DAOFactory.getUtenteDAO()

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(nomeUtente: String): UserDetails {
        val utente = userDAO.ottieniUtenteCompletoDaNomeUtente(nomeUtente)

        if (utente != null) {
            return User(utente.nomeUtente, utente.password, AuthoritiesUtil.getAuthorities(utente))
        }
        throw UsernameNotFoundException("No Name: $nomeUtente")
    }

    fun criptaPasswordESalvaUtente(utente: ingsw.server.entity.Utente): Boolean {
        utente.password = bcryptEncoder.encode(utente.password)
        return userDAO.salvaNuovoUtente(utente)
    }
}