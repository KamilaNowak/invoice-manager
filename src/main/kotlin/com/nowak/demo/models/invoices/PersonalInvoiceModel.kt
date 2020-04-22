package com.nowak.demo.models.invoices

import com.nowak.demo.database.getLoggedUser
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.customers.CustomerModel
import tornadofx.*

class PersonalInvoiceModel : ItemViewModel<PersonalInvoice>() {

    val invoiceNo = bind { item?.invoiceNoProperty }
    val dateOfIssue = bind { item?.dateOfIssueProperty }
    val quantity = bind { item?.quantityProperty }
    val amount = bind { item?.amountProperty }
    val paymentMethod = bind { item?.paymentMethodProperty }
    val discount = bind { item?.discountProperty }
    val customer = bind { item?.customerProperty }
    val creator = bind { item?.creatorProperty }

    companion object Dto {
        fun convertPersonalInvoiceModelToDto(personalInvoiceModel: PersonalInvoiceModel, customerModel: CustomerModel): PersonalInvoice {
            return PersonalInvoice("", personalInvoiceModel.dateOfIssue.value,
                    personalInvoiceModel.quantity.value.toInt(),
                    personalInvoiceModel.amount.value.toLong(),
                    personalInvoiceModel.paymentMethod.value,
                    personalInvoiceModel.discount.value.toInt(),
                    Customer(0, customerModel.name.value,
                            customerModel.surname.value,
                            customerModel.email.value,
                            customerModel.phoneNumber.value.toLong(),
                            customerModel.address.value),
                    getLoggedUser()
            )
        }
    }
}