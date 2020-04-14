package com.nowak.demo.wizards

import com.nowak.demo.models.customers.CompanyModel
import tornadofx.*

class InvoiceDetailsWizardView : View("Invoice informations") {

    val companyModel : CompanyModel by inject()
    override val root = vbox {

    }
}
