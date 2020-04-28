package com.nowak.demo.models.customers

import com.nowak.demo.models.addresses.AddressDetails
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Company(id: Long =0,
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
        return "Company($idProperty, $companyNameProperty, $nipProperty, $addressProperty, $ownerProperty)"
    }
    public fun showAddress():String{
        return address.country+", "+ address.city+", "+ address.street+", "+address.building.toString()
    }
    public fun showOwner(): String{
        return owner.name+" "+ owner.surname+"\nE-mail: "+owner.email+"\nPhone: "+owner.phoneNumber+"\nPID: "+owner.pid
    }


}