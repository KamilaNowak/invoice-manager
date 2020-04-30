package com.nowak.demo.models.summary

import com.nowak.demo.models.items.ReceiverType
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.time.LocalDate
import tornadofx.*;

class InvoiceSummary(invoiceNo: String, dateOfIssue: LocalDate, amount: Int, receiver: String, link: String?) {
    val invoiceNoProperty = SimpleStringProperty(invoiceNo)
    val dateOfIssueProperty = SimpleObjectProperty<LocalDate>(dateOfIssue)
    val amountProperty = SimpleIntegerProperty(amount)
    val receiverProperty = SimpleStringProperty(receiver)
    val linkProperty = SimpleStringProperty(link)
    val typeProperty = SimpleObjectProperty<ReceiverType>()
}

class InvoiceSummaryModel : ItemViewModel<InvoiceSummary>() {
    val invoiceNo = bind { item?.invoiceNoProperty }
    val dateOfIssue = bind { item?.dateOfIssueProperty }
    val amount = bind { item?.amountProperty }
    val receiver = bind { item?.receiverProperty }
    val link = bind { item?.linkProperty }
    val type = bind { item?.typeProperty }
}