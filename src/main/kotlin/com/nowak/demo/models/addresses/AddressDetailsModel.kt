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

    companion object Dto{
        fun convertAddressToDto(addressModel: AddressDetailsModel): AddressDetails{
            return AddressDetails(0, addressModel.country.value,
                    addressModel.city.value,
                    addressModel.street.value,
                    addressModel.building.value.toInt())
        }
    }
}