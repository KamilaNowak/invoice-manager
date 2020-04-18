package com.nowak.demo.models.login

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.time.LocalDate
import tornadofx.*;

class User(id: Long, username: String, password: String, email: String, birthDate: LocalDate, var createdAt: LocalDate){

    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val userNameProperty = SimpleStringProperty(username)
    var username by userNameProperty

    val passwordProperty  = SimpleStringProperty(password)
    var password  by passwordProperty

    val emailProperty = SimpleStringProperty(email)
    var email by emailProperty

    val birthDateProperty = SimpleObjectProperty<LocalDate>(birthDate)
    var birthDate by birthDateProperty

    override fun toString(): String {
        return "User (${id},${username},${password},${email},${birthDate},${createdAt})\n"
    }
}
