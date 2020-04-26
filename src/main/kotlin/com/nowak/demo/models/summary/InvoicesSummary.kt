package com.nowak.demo.models.summary

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.time.LocalDate
import tornadofx.*;

class InvoiceSummary(invoiceNo: String, dateOfIssue: LocalDate, amount: Int, receiver: String, link: String?) {
    val invoiceNoProperty = SimpleStringProperty(invoiceNo)
    var invoiceNo by invoiceNoProperty

    val dateOfIssueProperty = SimpleObjectProperty<LocalDate>(dateOfIssue)
    var dateOfIssue by dateOfIssueProperty

    val amountProperty = SimpleIntegerProperty(amount)
    var amount by amountProperty

    val receiverProperty = SimpleStringProperty(receiver)
    var receiver by receiverProperty

    val linkProperty = SimpleStringProperty(link)
    var link by linkProperty
}

class InvoiceSummaryModel : ItemViewModel<InvoiceSummary>() {
    val invoiceNo = bind { item?.invoiceNoProperty }
    val dateOfIssue = bind { item?.dateOfIssueProperty }
    val amount = bind { item?.amountProperty }
    val receiver = bind { item?.receiverProperty }
    val link = bind{item?.linkProperty}
}