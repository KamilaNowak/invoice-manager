package com.nowak.demo.controllers

import com.nowak.demo.database.InvoicerDatabase
import com.nowak.demo.view.WorkspaceView

import tornadofx.*

class LoginController : Controller(){

    val invoiceDatabase = InvoicerDatabase()

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