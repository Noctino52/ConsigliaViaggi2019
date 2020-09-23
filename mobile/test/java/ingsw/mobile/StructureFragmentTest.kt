package ingsw.mobile

import ingsw.mobile.entity.Structure
import ingsw.mobile.fragment.StructureFragment
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.lang.IllegalArgumentException

class StructureFragmentTest {
    private val stellaVuota = R.drawable.ic_star_border_black_24dp
    private val stellaMedia = R.drawable.ic_star_half_black_24dp
    private val stellaPiena = R.drawable.ic_star_black_24dp

    private lateinit var strutturaFragment: StructureFragment

    @Before
    fun istanziaFragment() {
        val strutturaStub = Structure("", 2.0, 3.0)
        strutturaFragment = StructureFragment(strutturaStub)
    }

    @Test (expected = IllegalArgumentException::class)
    fun ottieniStellaConNumeroStellaMinoreDiUno() {
        strutturaFragment.ottieniStella(-1, 2f)
    }

    @Test (expected = IllegalArgumentException::class)
    fun ottieniStellaConNumeroStellaMaggioreDiCinque() {
        strutturaFragment.ottieniStella(42, 3.5f)
    }

    @Test (expected = IllegalArgumentException::class)
    fun ottieniStellaConValutazioneMinoreDiUno() {
        strutturaFragment.ottieniStella(2, -5f)
    }

    @Test (expected = IllegalArgumentException::class)
    fun ottieniStellaConValutazioneMaggioreDiCinque() {
        strutturaFragment.ottieniStella(4, 33f)
    }

    @Test
    fun ottieniStellaConParametriValidi() {
        val stella = strutturaFragment.ottieniStella(2, 4.5f)
        assertEquals(stellaPiena, stella)
    }

    // Test White box
    @Test (expected = IllegalArgumentException::class)
    fun whiteboxPath_2_3() {
        strutturaFragment.ottieniStella(-6, 2f)
    }

    @Test
    fun whiteboxPath_2_7_8() {
        val stella = strutturaFragment.ottieniStella(1, 4.5f)
        assertEquals(stellaPiena, stella)
    }

    @Test
    fun whiteboxPath_2_9_10() {
        val stella = strutturaFragment.ottieniStella(3, 2.5f)
        assertEquals(stellaMedia, stella)
    }

    @Test
    fun whiteboxPath_2_9_11() {
        val stella = strutturaFragment.ottieniStella(5, 4f)
        assertEquals(stellaVuota, stella)
    }
}
