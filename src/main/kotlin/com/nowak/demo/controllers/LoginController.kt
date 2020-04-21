package com.nowak.demo.controllers

import com.nowak.demo.database.UserDatabase
import tornadofx.*

class LoginController : Controller(){

    private val userDatabase = UserDatabase()

    fun login(username: String, password:String) :Boolean{
        return userDatabase
                .checkIfAccountExists(username, password)
    }
    fun getLoggedUser(username: String): Long{
        val user = userDatabase
                .findUserByUsername(username)
        return user.id
    }
}