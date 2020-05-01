package com.nowak.demo.view


import com.nowak.demo.aws.S3Uploader
import com.nowak.demo.controllers.InvoiceController
import com.nowak.demo.controllers.InvoicesShowcaseController
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.models.summary.InvoiceSummary
import com.nowak.demo.models.summary.InvoiceSummaryModel
import com.nowak.demo.wizards.company.InvoiceDetailsWizardView
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import tornadofx.*

class InvoicesView : View("Invoices") {

    private val invoiceController: InvoiceController by inject()
    private val invoicesShowcaseController: InvoicesShowcaseController by inject()
    private val invInput = SimpleStringProperty()
    private val s3 = S3Uploader()

    override val root = vbox {
        stylesheets.add("styles.css")
        style { id = "third-scene-bg" }
        hbox {
            id = "vbox-organized"
            button(" New company Invoice") {
                graphic = FontAwesomeIconView(FontAwesomeIcon.CLIPBOARD)
                id = "dashboard-button"
                action { invoiceController.openAddCompanyInvoiceWizard() }
            }
            button(" New personal Invoice") {
                graphic = FontAwesomeIconView(FontAwesomeIcon.CLIPBOARD)
                id = "dashboard-button"
                action { invoiceController.openAddPersonalInvoiceWizard() }
            }
        }
        label("Company Invoices")
        tableview<InvoiceSummaryModel> {
            items = invoicesShowcaseController.getCompanyInvoiceSummary()
            this.requestResize()
            column("Invoice number", InvoiceSummaryModel::invoiceNo).remainingWidth()
            column("Date", InvoiceSummaryModel::dateOfIssue).remainingWidth()
            column("Amount", InvoiceSummaryModel::amount).remainingWidth()
            column("Receiver", InvoiceSummaryModel::receiver).remainingWidth()
            column("Document", InvoiceSummaryModel::link) {
                cellFormat {
                    graphic = button("PDF") {
                        graphic = FontAwesomeIconView(FontAwesomeIcon.FILE_PDF_ALT)
                        id = "tableview-button"
                        action {
                            hostServices.showDocument(rowItem.link.value)
                        }
                    }
                }
            }.remainingWidth()
            column("Notifications", InvoiceSummaryModel::receiver) {
                cellFormat {
                    graphic = button("E-mail") {
                        graphic = FontAwesomeIconView(FontAwesomeIcon.SEND)
                        id = "tableview-button"
                        action {
                            val scope = Scope()
                            rowItem.type.value=ReceiverType.COMPANY
                            setInScope(rowItem, scope)
                            openInternalWindow<EmailSenderView>(scope)
                        }
                    }
                }
            }.remainingWidth()
        }
        label("Personal Clients Invoices")
        tableview<InvoiceSummaryModel> {
            items = invoicesShowcaseController.getPersonalInvoiceSummary()
            columnResizePolicy = SmartResize.POLICY

            column("Invoice number", InvoiceSummaryModel::invoiceNo).remainingWidth()
            column("Date", InvoiceSummaryModel::dateOfIssue).remainingWidth()
            column("Amount", InvoiceSummaryModel::amount).remainingWidth()
            column("Receiver", InvoiceSummaryModel::receiver).remainingWidth()
            column("Document", InvoiceSummaryModel::link) {
                cellFormat {
                    graphic = button("PDF") {
                        graphic = FontAwesomeIconView(FontAwesomeIcon.FILE_PDF_ALT)
                        id = "tableview-button"
                        action {
                            hostServices.showDocument(rowItem.link.value)
                        }
                    }
                }
            }.remainingWidth()
            column("Notifications", InvoiceSummaryModel::receiver) {
                cellFormat {
                    graphic = button("E-mail") {
                        graphic = FontAwesomeIconView(FontAwesomeIcon.SEND)
                        id = "tableview-button"
                        action {
                            val scope = Scope()
                            rowItem.type.value=ReceiverType.PERSON
                            setInScope(rowItem, scope)
                            openInternalWindow<EmailSenderView>(scope)
                        }
                    }
                }
            }.remainingWidth()
        }

    }

}


