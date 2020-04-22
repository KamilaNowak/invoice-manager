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
    val phoneNumber = bind{
        item?.phoneNumberProperty
    }
    val pid =bind{
        item?.pidProperty
    }

  companion object Dto{
      fun convertOwnerModelToDto(ownerModel: OwnerModel):Owner{
          return Owner(0, ownerModel.name.value,
                  ownerModel.surname.value,
                  ownerModel.email.value,
                  ownerModel.phoneNumber.value.toLong(),
                  ownerModel.pid.value.toInt())
      }
  }
}