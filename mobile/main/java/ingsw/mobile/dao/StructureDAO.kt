package ingsw.mobile.dao

import ingsw.mobile.entity.Structure

interface StructureDAO {
    fun getAllStructure(): List<Structure>
    fun getStructureThatHaveString(nomeStruttura: String): List<Structure>
}