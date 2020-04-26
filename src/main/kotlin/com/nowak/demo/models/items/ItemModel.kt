package com.nowak.demo.models.items

import tornadofx.*

class ItemModel : ItemViewModel<Item>() {

    val id = bind { item?.idProperty }
    val description = bind { item?.descriptionProperty }
    val vat = bind{item?.vatProperty}
    val category = bind{ item?.categoryProperty}

    companion object Dto{
        fun convertItemModelToDto(itemModel: ItemModel):Item{
            return Item(0,
                    itemModel.description.value,
                    itemModel.vat.value.toInt(),
                    itemModel.category.value, "")
        }
    }
}