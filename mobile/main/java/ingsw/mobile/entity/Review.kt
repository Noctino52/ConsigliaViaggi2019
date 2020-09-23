package ingsw.mobile.entity

import java.lang.IllegalArgumentException
import java.util.Date

class Review(
    valutazione: Int,
    testo: String,
    var dataCreazione: Date,
    var autore: User,
    var struttura: Structure
) {
    var valutazione = valutazione
        set(value) {
            if(value < 1 || value > 5) {
                throw IllegalArgumentException()
            }
            field = value
        }

    var testo = testo
        set(value) {
            if(value.length < 100) {
                throw IllegalArgumentException()
            }
            field = value
        }
}