package ingsw.server.jwt.config

import org.springframework.security.core.AuthenticationException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import kotlin.jvm.Throws

@Component
class JwtAutenticazioneEntryPoint: AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        println("OH NO!!!")
        //UTENTE NON AUTENTICATO CORRETTAMENTE (JWT)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}