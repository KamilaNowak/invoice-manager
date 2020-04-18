package com.nowak.demo.controllers

import com.nowak.demo.database.InvoiceDatabase
import com.nowak.demo.models.login.User
import javafx.collections.FXCollections
import tornadofx.*
import java.time.LocalDate

class RegisterController : Controller() {

    private val invoiceDatabase = InvoiceDatabase()


    fun register(username: String, password: String, email: String, birthDate: LocalDate): Boolean {
        return if (!invoiceDatabase.checkUsernameEmailAvailability(username = username, email = email)) {
            invoiceDatabase.insertNewUser(username, password, email, birthDate)
            true
        } else {
            false
        }
    }
}