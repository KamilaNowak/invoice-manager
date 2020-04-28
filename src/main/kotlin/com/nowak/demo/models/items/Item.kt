package com.nowak.demo.models.items

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*;
class Item(id: Long, description:String, cost: Long, quantity: Long,  vat: Int, category: ItemCategory, invoiceNo: String) {

    val idProperty = SimpleLongProperty(id)
    var id by idProperty

    val vatProperty = SimpleIntegerProperty(vat)
    var vat by vatProperty

    val descriptionProperty = SimpleStringProperty(description)
    var description by descriptionProperty

    val costProperty = SimpleLongProperty(cost)
    var cost by costProperty

    val quantityProperty = SimpleLongProperty(quantity)
    var quantity by quantityProperty

    val categoryProperty = SimpleObjectProperty<ItemCategory>(category)
    var category by categoryProperty

    val invoiceNoProperty = SimpleStringProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    override fun toString(): String {
        return "Item($idProperty, $vatProperty, $descriptionProperty, $categoryProperty, $invoiceNoProperty)"
    }


}