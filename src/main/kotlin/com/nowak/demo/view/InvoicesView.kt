package com.nowak.demo.view


import com.nowak.demo.controllers.InvoiceController
import com.nowak.demo.controllers.InvoicesShowcaseController
import com.nowak.demo.models.summary.InvoiceSummary
import com.nowak.demo.models.summary.InvoiceSummaryModel
import tornadofx.*

class InvoicesView : View("Invoices") {

    private val invoiceController: InvoiceController by inject()
    private val invoicesShowcaseController: InvoicesShowcaseController by inject()

    override val root = vbox {
        stylesheets.add("styles.css")
        style { id = "third-scene-bg" }
        hbox {
            id = "vbox-organized"
            button(" New company Invoice") {
                id = "dashboard-button"
                action { invoiceController.openAddCompanyInvoiceWizard() }
            }
            button(" New personal Invoice") {
                id = "dashboard-button"
                action { invoiceController.openAddPersonalInvoiceWizard() }
            }
        }
        tableview<InvoiceSummaryModel> {
            items = invoicesShowcaseController.getCompanyInvoiceSummary()
            columnResizePolicy = SmartResize.POLICY

            column("Invoice number", InvoiceSummaryModel::invoiceNo)
            column("Date", InvoiceSummaryModel::dateOfIssue)
            column("Amount", InvoiceSummaryModel::amount)
            column("Receiver", InvoiceSummaryModel::receiver)
            column("Document", InvoiceSummaryModel::link)

        }

    }
}