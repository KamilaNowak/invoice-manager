package com.nowak.demo.models.customers

import tornadofx.*

class CompanyModel :ItemViewModel<Company>(){
    val id= bind{
        item?.idProperty
    }
    val companyName = bind{
        item?.companyNameProperty
    }
    val nip = bind{
        item?.companyNameProperty
    }
    val address = bind{
        item?.addressProperty
    }
    val owner = bind{
        item?.ownerProperty
    }
}