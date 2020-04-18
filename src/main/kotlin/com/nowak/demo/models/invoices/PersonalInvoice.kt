package com.nowak.demo.models.invoices

import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.login.User
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.time.LocalDate
import tornadofx.*;

class PersonalInvoice(invoiceNo: Long,
                      dateOfIssue: LocalDate,
                      quantity: Int,
                      amount: Long,
                      paymentMethod: PaymentMethod,
                      customer: Customer,
                      creator: User){
    val invoiceNoProperty = SimpleLongProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    val dateOfIssueProperty = SimpleObjectProperty<LocalDate>(dateOfIssue)
    var dateOfIssue by dateOfIssueProperty

    val quantityProperty = SimpleIntegerProperty(quantity)
    var quantity by quantityProperty

    val amountProperty = SimpleLongProperty(amount)
    var amount by amountProperty

    val paymentMethodProperty =SimpleObjectProperty<PaymentMethod>(paymentMethod)
    var paymentMethod by paymentMethodProperty

    val customerProperty = SimpleObjectProperty<Customer>(customer)
    var customer by customerProperty

    val creatorProperty = SimpleObjectProperty<User>(creator)
    var creator by creatorProperty

}