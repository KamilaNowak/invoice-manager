package com.nowak.demo.controllers

import com.nowak.demo.database.InvoicesDatabase
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.invoices.PersonalInvoice
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.wizards.company.CompanyInvoiceWizard
import com.nowak.demo.wizards.personal.PersonalInvoiceWizard
import javafx.collections.ObservableList
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
    fun addNewCompanyInvoice(companyInvoice: CompanyInvoice, items: ObservableList<Item>) {
        invoicesDatabase.insertCompanyInvoice(companyInvoice, items)
    }
    fun addNewPersonalInvoice(personalInvoice: PersonalInvoice, items: ObservableList<Item>){
        invoicesDatabase.insertPersonalInvoice(personalInvoice, items)
    }
    fun getEmail(invoiceNo:String, receiverType: ReceiverType):String{
        return invoicesDatabase.getEmailByInvoiceNo(invoiceNo, receiverType)!!
    }

}