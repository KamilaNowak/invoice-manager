package com.nowak.demo.database

import com.nowak.demo.controllers.UserController
import com.nowak.demo.models.login.User
import com.nowak.demo.view.loggedUser
import org.mindrot.jbcrypt.BCrypt
import sun.net.idn.StringPrep
import java.sql.Date
import java.time.LocalDate

val userDatabase = UserDatabase()

fun convertToDate(dateToConvert: LocalDate?): Date? {
    return Date.valueOf(dateToConvert)
}

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun verifyPassword(plainPassword: String, hashPassword: String): Boolean {
    return BCrypt.checkpw(plainPassword, hashPassword)
}

fun getLoggedUser(): User {
    return userDatabase.findUserById(loggedUser)
}