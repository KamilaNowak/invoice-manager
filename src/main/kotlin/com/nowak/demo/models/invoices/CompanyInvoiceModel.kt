package com.nowak.demo.models.invoices

import com.nowak.demo.database.getLoggedUser
import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.CompanyModel
import tornadofx.*

class CompanyInvoiceModel : ItemViewModel<CompanyInvoice>() {

    val invoiceNo = bind { item?.invoiceNoProperty }
    val dateOfIssue = bind { item?.dateOfIssueProperty }
    val amount = bind { item?.amountProperty }
    val paymentMethod = bind { item?.paymentMethodProperty }
    val company = bind { item?.companyProperty }
    val creator = bind { item?.creatorProperty }

    companion object Dto{
        fun convertCompanyModelToDto(companyInvoiceModel: CompanyInvoiceModel, companyModel: CompanyModel): CompanyInvoice{
            return CompanyInvoice("",
                    companyInvoiceModel.dateOfIssue.value,
                    companyInvoiceModel.amount.value.toLong(),
                    companyInvoiceModel.paymentMethod.value,
                    Company(0,companyModel.companyName.value,
                            companyModel.nip.value.toLong(),companyModel.address.value,
                            companyModel.owner.value),
                    getLoggedUser()
            )
        }
    }
}