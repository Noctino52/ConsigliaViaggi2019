package ingsw.mobile.dao

import android.content.Context
import ingsw.mobile.R
import ingsw.mobile.entity.Structure
import ingsw.mobile.entity.ClientHttp
import ingsw.mobile.entity.JsonUtil
import okhttp3.HttpUrl
import java.util.Properties

class StructureDAOServerSpring(context: Context): StructureDAO {
    private val baseUrl: String

    init {
        val rawResource = context.resources.openRawResource(R.raw.config)
        val props = Properties()
        props.load(rawResource)
        baseUrl = "${props.getProperty("baseUrl")}/struttura"
    }

    override fun getAllStructure(): List<Structure> {
        val endPoint = "${baseUrl}/ottieniTutteStrutture"
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder().build()
        println(httpUrl.toString())
        val json = ClientHttp.doGetRequest(httpUrl)
        return JsonUtil.fullListStructureJSON(json)
    }

    override fun getStructureThatHaveString(nomeStruttura: String): List<Structure> {
        val endPoint = "${baseUrl}/ottieniStruttureCheContengonoString"
        val httpUrl = HttpUrl.parse(endPoint)!!.newBuilder()
            .addQueryParameter("nomeStruttura", nomeStruttura)
            .build()

        val json = ClientHttp.doGetRequest(httpUrl)
        return JsonUtil.fullListStructureJSON(json)
    }
}
