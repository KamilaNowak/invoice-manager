package com.nowak.demo.controllers

import com.nowak.demo.database.InvoicerDatabase
import com.nowak.demo.models.login.User
import tornadofx.*
import java.time.LocalDate

class RegisterController : Controller() {

    val invoiceDatabase = InvoicerDatabase()

    fun register(username: String, password: String, email: String, birthDate: LocalDate): Boolean {
        if (!invoiceDatabase.checkUsernameEmailAvailability(username = username, email = email)) {
            invoiceDatabase.insertNewUser(username, password, email, birthDate)
            return true
        } else {
            return false
        }
    }
}