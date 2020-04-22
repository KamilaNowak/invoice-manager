package com.nowak.demo.models.customers

import com.nowak.demo.database.getLoggedUser
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.invoices.CompanyInvoiceModel
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