package com.nowak.demo.models.invoices

import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.login.User
import javafx.beans.property.*
import javafx.collections.FXCollections
import tornadofx.*
import java.time.LocalDate

class CompanyInvoice(invoiceNo: String,
                     dateOfIssue: LocalDate,
                     amount: Long,
                     paymentMethod: PaymentMethod,
                     company: Company,
                     creator: User) {
    val invoiceNoProperty = SimpleStringProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    val dateOfIssueProperty = SimpleObjectProperty<LocalDate>(dateOfIssue)
    var dateOfIssue by dateOfIssueProperty

    val amountProperty = SimpleLongProperty(amount)
    var amount by amountProperty

    val paymentMethodProperty = SimpleObjectProperty<PaymentMethod>(paymentMethod)
    var paymentMethod by paymentMethodProperty

    val companyProperty = SimpleObjectProperty<Company>(company)
    var company by companyProperty

    val creatorProperty = SimpleObjectProperty<User>(creator)
    var creator by creatorProperty

    var items = ArrayList<Item>()

}