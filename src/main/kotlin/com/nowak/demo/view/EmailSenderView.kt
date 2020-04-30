package com.nowak.demo.view

import com.nowak.demo.controllers.InvoiceController
import com.nowak.demo.mailing.MailSender
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.models.summary.InvoiceSummaryModel
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import sun.java2d.pipe.SpanShapeRenderer
import tornadofx.*

class EmailSenderView : View() {
    private val message = SimpleStringProperty("")
    private val status = SimpleStringProperty("")
    private val invoiceModel: InvoiceSummaryModel by inject()
    private val invoiceController: InvoiceController by inject()
    private val mailSender = MailSender()

    override val root = vbox {
        style {
            stylesheets.add("styles.css")
            style { id = "modal" }
            paddingAll = 20.0
            spacing = 15.px
        }
        label("Your message to receiver") { id = "info-label" }
        textarea(message) {
            id = "text-field"
            promptText = "You can write custom message to receiver or leave default..."
            requestFocus()
        }
        label {
            text = invoiceModel.invoiceNo.value
            graphic = FontAwesomeIconView(FontAwesomeIcon.FILE_PDF_ALT)
        }
        hbox {
            id = "vbox-organized"
            button("Send") {
                id = "dashboard-button"
                graphic = FontAwesomeIconView(FontAwesomeIcon.SHARE)
                action {
                    status.value = mailSender.sendEmail(invoiceController.getEmail(invoiceModel.invoiceNo.value, invoiceModel.type.value),
                            invoiceModel.invoiceNo.value,
                            message.value,
                            invoiceModel.link.value)
                }
            }
            label().bind(status).apply {
                style { id = "status-label" }
            }
        }
    }
}