package com.nowak.demo.wizards

import com.nowak.demo.models.customers.CompanyModel
import com.nowak.demo.models.invoices.CompanyInvoiceModel
import com.nowak.demo.models.invoices.PaymentMethod
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*

class InvoiceDetailsWizardView : View("Invoice informations") {

    private val companyModel: CompanyModel by inject()
    private val companyInvoiceModel = CompanyInvoiceModel()
    override fun onSave() {
        isComplete = companyInvoiceModel.commit()
    }
    override val root = vbox {

        label("Invoice details")
        form {

            style{ stylesheets.add("styles.css")}
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
