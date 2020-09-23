package ingsw.mobile.entity

import android.content.Context
import ingsw.mobile.authentication.AuthFactory
import ingsw.mobile.authentication.AuthProvider
import okhttp3.*

object ClientHttp {
    var context: Context? = null
    set(value) {
        if(value != null) {
            field = value
            authFactory = AuthFactory(value)
            authProvider = authFactory.getAuthProvider()
        }
    }

    private val mediaTypeJSON = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()
    private lateinit var authFactory: AuthFactory
    private lateinit var authProvider: AuthProvider

    private fun getResponseBody(request: Request): String? {
        var response: Response? = null

        try {
            response = client.newCall(request).execute()

            if(response?.code() != 200) {
                return null
            }
            return response.body()?.string()
        }
        catch (e: Throwable) {
            return null
        }
        finally {
            response?.body()?.close()
        }
    }

    fun doGetRequest(httpUrl: HttpUrl): String? {
        val request = Request.Builder().url(httpUrl).build()

        return getResponseBody(request)
    }


    fun doPostWithBodyRequestByJSON(endPoint: String, bodyJSON: String,
                                    conAutenticazione: Boolean): String? {
        println(endPoint+bodyJSON)
        val requestBody = RequestBody.create(mediaTypeJSON, bodyJSON)
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder().build()
        val request = Request.Builder().url(httpUrl).post(requestBody)

        if(conAutenticazione) {
            authProvider.addAuthRequestHttp(request)
        }

        return getResponseBody(request.build())
    }
}