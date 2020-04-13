package com.nowak.demo.models.customers

import tornadofx.*

class OwnerModel : ItemViewModel<Owner>(){
    val id = bind{
        item?.idProperty
    }
    val name = bind{
        item?.nameProperty
    }
    val surname = bind{
        item?.surnameProperty
    }
    val email = bind{
        item?.emailProperty
    }
    val pid =bind{
        item?.pidProperty
    }
}