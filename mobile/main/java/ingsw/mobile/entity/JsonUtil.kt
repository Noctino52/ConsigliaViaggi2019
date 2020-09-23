package ingsw.mobile.entity

import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

object JsonUtil {
    private val jsonConverter = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

    fun fullListUserJSON(json: String?): List<User> {
        val jsonArray = JSONArray(json)
        val listaUtente = mutableListOf<User>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val utente = jsonConverter.fromJson(jsonObject.toString(), User::class.java)
            listaUtente.add(utente)
        }

        return listaUtente
    }

    fun fullListStructureJSON(json: String?): List<Structure> {
        val jsonArray = JSONArray(json)
        val listaStrutture = mutableListOf<Structure>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val struttura = jsonConverter.fromJson(jsonObject.toString(), Structure::class.java)
            listaStrutture.add(struttura)
        }

        return listaStrutture
    }


    fun getListRecOfAStructByJSON(json: String?, structure: Structure):
            MutableList<Review> {
        if(json == null) { return mutableListOf() }

        val jsonArray = JSONArray(json)
        val listaRecensioni = mutableListOf<Review>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val utente = getUserFromJSONObject(jsonObject)
            val recensione = fullReviewByJSON(jsonObject.toString(), utente, structure)!!
            listaRecensioni.add(recensione)
        }

        return listaRecensioni
    }

    private fun fullReviewByJSON(json: String?, user: User,
                                 structure: Structure): Review? {
        val recensione = jsonConverter.fromJson(json, Review::class.java)
        recensione?.autore = user
        recensione?.struttura = structure
        return recensione
    }

    private fun getUserFromJSONObject(jsonObject: JSONObject): User {
        return jsonConverter.fromJson(jsonObject.getJSONObject("autore").toString(),
            User::class.java)
    }

    fun setRecByJSON(endPoint: String, review: Review): Boolean {
        val recensioneJson = jsonConverter.toJson(review, Review::class.java)
        val esito = ClientHttp.doPostWithBodyRequestByJSON(endPoint, recensioneJson,
            conAutenticazione = true)
        return esito?.toBoolean() ?: false
    }
}