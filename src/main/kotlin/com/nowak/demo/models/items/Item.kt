package com.nowak.demo.models.items

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*;
class Item(id: Long, description:String, vat: Int, category: ItemCategory, invoiceNo: String) {

    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val vatProperty = SimpleIntegerProperty(vat)
    var vat by vatProperty

    val descriptionProperty = SimpleStringProperty(description)
    var description by descriptionProperty

    val categoryProperty = SimpleObjectProperty<ItemCategory>(category)
    var category by categoryProperty

    val invoiceNoProperty = SimpleStringProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    override fun toString(): String {
        return "Item($idProperty, $vatProperty, $descriptionProperty, $categoryProperty, $invoiceNoProperty)"
    }


}