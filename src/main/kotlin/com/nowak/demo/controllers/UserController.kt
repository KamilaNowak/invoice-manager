package com.nowak.demo.controllers


import com.nowak.demo.database.UserDatabase
import com.nowak.demo.database.verifyPassword
import com.nowak.demo.models.login.User
import com.nowak.demo.view.loggedUser
import tornadofx.*
import java.time.LocalDate

class UserController : Controller() {

    private val userDatabase = UserDatabase()

    fun findUserById(id: Long): User {
        return userDatabase.findUserById(id)
    }

    fun updateUser(userId: Long, newUsername: String? = null, newEmail: String? = null, newBirthDate: LocalDate? = null, newPassword: String? = null): Boolean {
        return userDatabase.updateUser(userId, newUsername, newEmail, newBirthDate, newPassword)
    }

    fun checkUsernameEmailAvailability(username: String? = null, email: String? = null): Boolean {
        return userDatabase.checkUsernameEmailAvailability(username, email)
    }
    fun confirmUser(password: String):Boolean{
        return verifyPassword(password, userDatabase.getPasswordByUserId(loggedUser))
    }

}