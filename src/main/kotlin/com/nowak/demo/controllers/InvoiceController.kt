package com.nowak.demo.controllers

import com.nowak.demo.database.InvoicesDatabase
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.invoices.CompanyInvoiceModel
import com.nowak.demo.models.invoices.PersonalInvoice
import com.nowak.demo.models.items.Item
import com.nowak.demo.wizards.company.CompanyInvoiceWizard
import com.nowak.demo.wizards.personal.PersonalInvoiceWizard
import tornadofx.*

class InvoiceController : Controller() {

    private val invoicesDatabase = InvoicesDatabase()

    fun openAddCompanyInvoiceWizard() {
        find<CompanyInvoiceWizard> {
            onComplete { information("Invoice successfully generated") }
            openModal()
        }
    }

    fun openAddPersonalInvoiceWizard(){
        find<PersonalInvoiceWizard>{
            onComplete { information("Invoice successfully generated")}
            openModal()
        }
    }
    fun addNewCompanyInvoice(companyInvoice: CompanyInvoice, item: Item) {
        invoicesDatabase.insertCompanyInvoice(companyInvoice, item)
    }
    fun addNewPersonalInvoice(personalInvoice: PersonalInvoice, item: Item){
        invoicesDatabase.insertPersonalInvoice(personalInvoice, item)
    }
}