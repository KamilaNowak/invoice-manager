package com.nowak.demo.controllers
import com.nowak.demo.database.UserDatabase
import com.nowak.demo.models.login.User
import javafx.collections.FXCollections
import tornadofx.*
import java.time.LocalDate

class RegisterController : Controller() {

    private val userDatabase = UserDatabase()

    fun register(username: String, password: String, email: String, birthDate: LocalDate): Boolean {
        return if (!userDatabase.checkUsernameEmailAvailability(username = username, email = email)) {
            userDatabase.insertNewUser(username, password, email, birthDate)
            true
        } else false
    }
}