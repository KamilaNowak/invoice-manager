package com.nowak.demo.models.items

import tornadofx.*

class ItemModel : ItemViewModel<Item>() {

    val id = bind { item?.idProperty }
    val description = bind { item?.descriptionProperty }
    val vat = bind{item?.vatProperty}
    val category = bind{ item?.categoryProperty}
    val invoiceNo = bind{item?.invoiceNoProperty}
}