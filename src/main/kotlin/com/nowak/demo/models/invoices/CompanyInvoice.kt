package com.nowak.demo.models.invoices

import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.login.User
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate

class CompanyInvoice(invoiceNo: Long,
                     dateOfIssue: LocalDate,
                     quantity: Int,
                     amount: Long,
                     paymentMethod: PaymentMethod,
                     company: Company,
                     creator: User){
    val invoiceNoProperty = SimpleLongProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    val dateOfIssueProperty = SimpleObjectProperty<LocalDate>(dateOfIssue)
    var dateOfIssue by dateOfIssueProperty

    val quantityProperty = SimpleIntegerProperty(quantity)
    var quantity by quantityProperty

    val amountProperty = SimpleLongProperty(amount)
    var amount by amountProperty

    val paymentMethodProperty = SimpleObjectProperty<PaymentMethod>(paymentMethod)
    var paymentMethod by paymentMethodProperty

    val companyProperty = SimpleObjectProperty<Company>(company)
    var company by companyProperty

    val creatorProperty = SimpleObjectProperty<User>(creator)
    var creator by creatorProperty

}