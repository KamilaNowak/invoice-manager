package com.nowak.demo.models.invoices

import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.login.User
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.time.LocalDate
import tornadofx.*;

class PersonalInvoice(invoiceNo: String,
                      dateOfIssue: LocalDate,
                      amount: Long,
                      paymentMethod: PaymentMethod,
                      discount:Int,
                      customer: Customer,
                      creator: User){
    val invoiceNoProperty = SimpleStringProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    val dateOfIssueProperty = SimpleObjectProperty<LocalDate>(dateOfIssue)
    var dateOfIssue by dateOfIssueProperty

    val amountProperty = SimpleLongProperty(amount)
    var amount by amountProperty

    val paymentMethodProperty =SimpleObjectProperty<PaymentMethod>(paymentMethod)
    var paymentMethod by paymentMethodProperty

    val discountProperty = SimpleIntegerProperty(discount)
    var discount by discountProperty

    val customerProperty = SimpleObjectProperty<Customer>(customer)
    var customer by customerProperty

    val creatorProperty = SimpleObjectProperty<User>(creator)
    var creator by creatorProperty

    var items = ArrayList<Item>()

}