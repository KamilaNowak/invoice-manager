package com.nowak.demo.view


import com.nowak.demo.controllers.InvoiceController
import tornadofx.*

class CompanyInvoicesView : View("Company Invoices") {

    val invoiceController: InvoiceController by inject()

    override val root = vbox {
        style {
            id = "third-scene-bg"
            stylesheets.add("styles.css")
        }
        button(" New company Invoice") {
            id = "dashboard-button"
            action {
                invoiceController.addNewCompanyInvoice()
            }
        }
    }
}