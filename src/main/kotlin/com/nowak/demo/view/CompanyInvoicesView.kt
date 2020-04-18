package com.nowak.demo.view


import com.nowak.demo.controllers.InvoiceController
import tornadofx.*

class CompanyInvoicesView : View("Company Invoices") {

    private val invoiceController: InvoiceController by inject()

    override val root = vbox {
        stylesheets.add("styles.css")
        style { id = "third-scene-bg" }
        button(" New company Invoice") {
            id = "dashboard-button"
            action { invoiceController.addNewCompanyInvoice() }
        }
    }
}