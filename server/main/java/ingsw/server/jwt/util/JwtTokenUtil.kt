package ingsw.server.jwt.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import ingsw.server.dao.DAOFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

@Component
class JwtTokenUtil {
    private val props = Properties()
    private val secret: String
    private val tipoPersistenza : String
    private val jwtTokenValidity: Long

    init {
        val fileInputStream = DAOFactory::class.java.classLoader.getResourceAsStream("config.properties")
        props.load(fileInputStream)
        secret = props.getProperty("jwt.secret")
        tipoPersistenza = props.getProperty("tipoPersistenza")
        jwtTokenValidity = props.getProperty("jwt.tokenValidity").toLong()
    }


    fun ottieniDataCreazioneDaToken(token: String?): Date {
        return getClaimFromToken(token, Function { obj: Claims -> obj.issuedAt })
    }

    fun ottieniNomeUtenteDaToken(token: String?): String {
        return getClaimFromToken(token, Function { obj: Claims -> obj.subject })
    }

    private fun ottieniDataScadenzaDaToken(token: String?): Date {
        return getClaimFromToken(token, Function { obj: Claims -> obj.expiration })
    }

    private fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
    }

    private fun tokenNonScaduto(token: String): Boolean {
        val expiration = ottieniDataScadenzaDaToken(token)
        return !expiration.before(Date())
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        return false
    }
    fun canTokenBeRefreshed(token: String): Boolean {
        return tokenNonScaduto(token) || ignoreTokenExpiration(token)
    }

    fun generaToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return creaNuovoToken(claims, userDetails.username)
    }

    private fun creaNuovoToken(claims: Map<String, Any>, nomeUtenteAutenticato: String): String {
        val dataCreazioneToken = Date(System.currentTimeMillis())
        val dataScadenzaToken = Date(System.currentTimeMillis() + jwtTokenValidity * 1000)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(nomeUtenteAutenticato)
                .setIssuedAt(dataCreazioneToken)
                .setExpiration(dataScadenzaToken)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }


    fun verificaTokenValido(token: String, userDetails: UserDetails): Boolean {
        val username = ottieniNomeUtenteDaToken(token)
        return username == userDetails.username && tokenNonScaduto(token)
    }
}