package com.nowak.demo.models.customers

import com.nowak.demo.models.addresses.AddressDetails
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.mail.Address

class CustomerModel() : ItemViewModel<Customer>() {

    val id = bind { item?.idProperty }
    val name = bind { item?.nameProperty }
    val surname = bind { item?.surnameProperty }
    val email = bind { item?.emailProperty }
    val phoneNumber = bind { item?.phoneNumberProperty }
    val address = bind { item?.addressProperty }
}