package com.nowak.demo.view


import com.nowak.demo.controllers.InvoiceController
import tornadofx.*

class InvoicesView : View("Invoices") {

    private val invoiceController: InvoiceController by inject()

    override val root = vbox {
        stylesheets.add("styles.css")
        style { id = "third-scene-bg" }
        hbox {
            button(" New company Invoice") {
                id = "dashboard-button"
                action { invoiceController.openAddCompanyInvoiceWizard() }
            }
            button(" New personal Invoice") {
                id = "dashboard-button"
                action { invoiceController.openAddPersonalInvoiceWizard() }
            }
        }
    }
}