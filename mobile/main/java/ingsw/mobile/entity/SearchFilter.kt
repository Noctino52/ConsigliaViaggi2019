package ingsw.mobile.entity

data class SearchFilter (
    var sort: Int,
    var starMin: Int,
    var maxKM: Float,
    var gpsON: Boolean,
    var query: String
)