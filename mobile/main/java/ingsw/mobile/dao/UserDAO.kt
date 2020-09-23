package ingsw.mobile.dao

import ingsw.mobile.entity.User

interface UserDAO {
    fun AvaibleEmail(email: String): Boolean?
    fun AvaibleUsername(nomeUtente: String): Boolean?
    fun getUsernameThatHaveString(nomeUtente : String): List<User>
}