package com.nowak.demo.wizards.personal

import com.nowak.demo.controllers.InvoiceController
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.customers.CustomerModel
import com.nowak.demo.models.invoices.PaymentMethod
import com.nowak.demo.models.invoices.PersonalInvoiceModel
import com.nowak.demo.models.items.ItemCategory
import com.nowak.demo.models.items.ItemModel
import javafx.collections.FXCollections
import tornadofx.*

class PersonalInvoiceDetailsWizardView : View("My View") {

    private val personalInvoiceModel: PersonalInvoiceModel by inject()
    private val itemModel: ItemModel by inject()
    private val invoiceController: InvoiceController by inject()
    private val customerModel: CustomerModel by inject()

    override fun onSave() {
        invoiceController.addNewPersonalInvoice(PersonalInvoiceModel
                .convertPersonalInvoiceModelToDto(personalInvoiceModel, customerModel),
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
                    datepicker(personalInvoiceModel.dateOfIssue) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Quantity") {
                    textfield(personalInvoiceModel.quantity) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Amount") {
                    textfield(personalInvoiceModel.amount) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Discount") {
                    textfield(personalInvoiceModel.discount) {
                        style { id = "text-field" }
                    }
                }
                field("PaymentMethod") {
                    combobox(personalInvoiceModel.paymentMethod) {
                        items = FXCollections.observableArrayList(PaymentMethod.values().toList())
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
    }
}
