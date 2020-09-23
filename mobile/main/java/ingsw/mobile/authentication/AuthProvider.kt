package ingsw.mobile.authentication

import okhttp3.Request

interface AuthProvider {
    fun obtainUsername(): String?
    fun doLogout()
    fun doAccess(nomeUtente: String, password: String): Boolean
    fun addAuthRequestHttp(request: Request.Builder)
    fun actuallyAuth(): Boolean
}