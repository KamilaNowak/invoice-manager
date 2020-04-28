package com.nowak.demo.models.addresses

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate

class AddressDetails(id: Long,
                     country: String,
                     city: String,
                     street:String,
                     building: Int) {
    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val countryProperty = SimpleStringProperty(country)
    var country by countryProperty

    val cityProperty = SimpleStringProperty(city)
    var city by cityProperty

    val streetProperty = SimpleStringProperty(street)
    var street by streetProperty

    val buildingProperty = SimpleIntegerProperty(building)
    var building by buildingProperty

    override fun toString(): String {
        return "$idProperty, $countryProperty, $cityProperty,$streetProperty, $buildingProperty)"
    }
    public fun showAddress(): String {
        return country+", "+city+", "+street+"  "+building.toString()
    }

}