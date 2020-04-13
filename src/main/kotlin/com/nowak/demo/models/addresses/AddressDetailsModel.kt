package com.nowak.demo.models.addresses

import tornadofx.*

class AddressDetailsModel : ItemViewModel<AddressDetails>(){
    val id = bind{
        item?.idProperty
    }
    val country = bind{
        item?.countryProperty
    }
    val city = bind{
        item?.cityProperty
    }
    val street = bind{
        item?.streetProperty
    }
    val building = bind{
        item?.buildingProperty
    }
}