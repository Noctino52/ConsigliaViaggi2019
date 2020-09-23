package ingsw.server.jwt.config

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import ingsw.server.jwt.service.UtenteDetailsService
import ingsw.server.jwt.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

@Component
class JwtFiltroRichieste : OncePerRequestFilter() {
    @Autowired
    private lateinit var utenteDetailsService: UtenteDetailsService
    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestTokenHeader = request.getHeader("Authorization")
        var token: String? = null
        var username: String? = null

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(7)
            username = ottieniNomeUtenteDaTokenJwt(token)
        }

        if (tokenValido(username)) {
            val userDetails = utenteDetailsService.loadUserByUsername(username!!)

            if (jwtTokenUtil.verificaTokenValido(token!!, userDetails)) {
                // Autenticazione tramite Spring
                val authenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities)
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }
        chain.doFilter(request, response)
    }

    private fun ottieniNomeUtenteDaTokenJwt(jwtToken: String): String? {
        try {
            return jwtTokenUtil.ottieniNomeUtenteDaToken(jwtToken)
        }
        catch (e: IllegalArgumentException) {
            println("Impossibile leggere il Token JWT")
        }
        catch (e: ExpiredJwtException) {
            println("Il Token JWT è scaduto")
        }
        catch (e: SignatureException) {
            println("Il Token JWT non valido")
        }
        catch (e: MalformedJwtException) {
            println("Il Token JWT è malformato")
        }
        return null
    }

    private fun tokenValido(username: String?): Boolean {
        return username != null && SecurityContextHolder.getContext().authentication == null
    }

}