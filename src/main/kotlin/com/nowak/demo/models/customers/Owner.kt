package com.nowak.demo.models.customers

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Owner(id: Long,
            name: String,
            surname: String,
            email: String,
            phoneNumber: Long,
            pid: Int) {
    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val surnameProperty = SimpleStringProperty(surname)
    var surname by surnameProperty

    val emailProperty = SimpleStringProperty(email)
    var email by emailProperty

    val phoneNumberProperty = SimpleLongProperty(phoneNumber)
    val phoneNumber by phoneNumberProperty

    val pidProperty = SimpleIntegerProperty(pid)
    var pid by pidProperty

    override fun toString(): String {
        return "Owner(idProperty=$idProperty, nameProperty=$nameProperty, surnameProperty=$surnameProperty, emailProperty=$emailProperty, pidProperty=$pidProperty), phoneNrProperty=${phoneNumber}"
    }


}