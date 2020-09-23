package ingsw.mobile.dao

import android.content.Context
import ingsw.mobile.R
import ingsw.mobile.entity.Review
import ingsw.mobile.entity.Structure
import ingsw.mobile.entity.ClientHttp
import ingsw.mobile.entity.JsonUtil
import okhttp3.HttpUrl
import java.util.Properties

class ReviewDAOServerSpring(context: Context): ReviewDAO {
    private val baseUrl: String

    init {
        val rawResource = context.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        baseUrl = "${props.getProperty("baseUrl")}/recensione"
    }

    override fun addNewReview(review: Review): Boolean {
        val endPoint = "$baseUrl/aggiungiNuovaRecensione"
        return JsonUtil.setRecByJSON(endPoint, review)
    }

    override fun getReviewStructure(structure: Structure): MutableList<Review> {
        val endPoint = "$baseUrl/ottieniRecensioniStruttura"
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder()
            .addQueryParameter("nomeStruttura", structure.nomeStruttura)
            .addQueryParameter("latitudine", structure.latitudine.toString())
            .addQueryParameter("longitudine", structure.longitudine.toString())
            .build()
        val json = ClientHttp.doGetRequest(httpUrl)
        return JsonUtil.getListRecOfAStructByJSON(json, structure)
    }
}
