package com.nowak.demo.controllers

import com.nowak.demo.wizards.CompanyInvoiceWizard
import tornadofx.*

class InvoiceController : Controller() {

    fun addNewCompanyInvoice(){
        find<CompanyInvoiceWizard>{
            onComplete {

            }
            openModal()
        }
    }
}