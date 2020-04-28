package com.nowak.demo.models.items

import tornadofx.*

class ItemModel : ItemViewModel<Item>() {

    val id = bind { item?.idProperty }
    val description = bind { item?.descriptionProperty }
    val vat = bind{item?.vatProperty}
    val category = bind{ item?.categoryProperty}
    val cost = bind{ item?.costProperty}
    val quantity = bind{item?.quantityProperty}

    companion object Dto{
        fun convertItemModelToDto(itemModel: ItemModel):Item{
            return Item(0,
                    itemModel.description.value,
                    itemModel.cost.value.toLong(),
                    itemModel.quantity.value.toLong(),
                    itemModel.vat.value.toInt(),
                    itemModel.category.value, "")
        }
    }
}