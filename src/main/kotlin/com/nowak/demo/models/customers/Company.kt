package com.nowak.demo.models.customers

import com.nowak.demo.models.addresses.AddressDetails
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Company(id: Long,
             companyName: String,
             nip:Long,
             address:AddressDetails,
             owner: Owner) {
    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val companyNameProperty = SimpleStringProperty(companyName)
    var companyName by companyNameProperty

    val nipProperty = SimpleLongProperty(nip)
    var nip by nipProperty

    val addressProperty = SimpleObjectProperty<AddressDetails>(address)
    var address by addressProperty

    val ownerProperty = SimpleObjectProperty<Owner>(owner)
    val owner by ownerProperty

    override fun toString(): String {
        return "Company(idProperty=$idProperty, companyNameProperty=$companyNameProperty, nipProperty=$nipProperty, addressProperty=$addressProperty, ownerProperty=$ownerProperty)"
    }


}