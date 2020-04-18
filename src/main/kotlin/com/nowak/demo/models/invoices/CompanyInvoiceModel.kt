package com.nowak.demo.models.invoices

import tornadofx.*

class CompanyInvoiceModel : ItemViewModel<CompanyInvoice>(){
     val invoiceNo = bind{
         item?.invoiceNoProperty
     }
    val dateOfIssue = bind{
        item?.dateOfIssueProperty
    }
    val quantity = bind{
        item?.quantityProperty
    }
    val amount = bind{
        item?.amountProperty
    }
    val paymentMethod = bind{
        item?.paymentMethodProperty
    }
    val company = bind{
        item?.companyProperty
    }
    val creator = bind{
        item?.creatorProperty
    }
}