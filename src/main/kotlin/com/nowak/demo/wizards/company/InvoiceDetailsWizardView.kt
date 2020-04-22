package com.nowak.demo.wizards.company

import com.nowak.demo.controllers.InvoiceController
import com.nowak.demo.models.customers.CompanyModel
import com.nowak.demo.models.invoices.CompanyInvoiceModel
import com.nowak.demo.models.invoices.PaymentMethod
import com.nowak.demo.models.items.ItemCategory
import com.nowak.demo.models.items.ItemModel
import javafx.collections.FXCollections
import tornadofx.*

class InvoiceDetailsWizardView : View("Invoice informations") {

    private val companyModel: CompanyModel by inject()
    private val companyInvoiceModel: CompanyInvoiceModel by inject()
    private val itemModel: ItemModel by inject()
    private val invoiceController: InvoiceController by inject()

    override fun onSave() {
        invoiceController.addNewCompanyInvoice(CompanyInvoiceModel
                .convertCompanyModelToDto(companyInvoiceModel,companyModel),
                ItemModel.convertItemModelToDto(itemModel))
    }

    override val root = vbox {

        label("Item details")
        form {
            fieldset {
                field("description") {
                    textfield(itemModel.description) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("VAT") {
                    textfield(itemModel.vat) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Category") {
                    combobox(itemModel.category) {
                        items = FXCollections.observableList(ItemCategory.values().toList())
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }

        label("Invoice details")
        form {

            style { stylesheets.add("styles.css") }
            fieldset {
                field("Date of issue") {
                    datepicker(companyInvoiceModel.dateOfIssue) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Quantity") {
                    textfield(companyInvoiceModel.quantity) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Amount") {
                    textfield(companyInvoiceModel.amount) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("PaymentMethod") {
                    combobox(companyInvoiceModel.paymentMethod) {
                        items = FXCollections.observableArrayList(PaymentMethod.values().toList())
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
    }
}

