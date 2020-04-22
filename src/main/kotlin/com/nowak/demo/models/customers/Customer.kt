package com.nowak.demo.models.customers

import com.nowak.demo.models.addresses.AddressDetails
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Customer(id: Long,
               name: String,
               surname: String,
               email: String,
               phoneNumber: Long,
               address: AddressDetails){
    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val surnameProperty = SimpleStringProperty(surname)
    var surname by surnameProperty

    val emailProperty = SimpleStringProperty(email)
    var email by emailProperty

    val phoneNumberProperty = SimpleLongProperty(phoneNumber)
    var phoneNumber by phoneNumberProperty

    val addressProperty = SimpleObjectProperty<AddressDetails>(address)
    var address by addressProperty

    override fun toString(): String {
        return "CustomerModel(idProperty=$idProperty, nameProperty=$nameProperty, surnameProperty=$surnameProperty, emailProperty=$emailProperty, phoneNumberProperty=$phoneNumberProperty, addressProperty=$addressProperty)"
    }
}
