package com.nowak.demo.controllers

import com.nowak.demo.database.InvoiceDatabase
import tornadofx.*

class LoginController : Controller(){

    private val invoiceDatabase = InvoiceDatabase()

    fun login(username: String, password:String) :Boolean{
        return invoiceDatabase
                .checkIfAccountExists(username, password)
    }
    fun getLoggedUser(username: String): Long{
        val user = invoiceDatabase
                .findUserByUsername(username)
        return user.id
    }
}