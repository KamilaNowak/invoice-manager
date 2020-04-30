package com.nowak.demo.wizards.personal

import com.nowak.demo.models.addresses.AddressDetailsModel
import com.nowak.demo.models.customers.CompanyModel
import com.nowak.demo.models.customers.CustomerModel
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import tornadofx.*

class CustomerDetailsWizardView : View("Customer informations"){

    private val customerModel: CustomerModel by inject()
    private val addressModel: AddressDetailsModel by inject()

    override fun onSave() {

        customerModel.address.value =
                SimpleObjectProperty(AddressDetailsModel.convertAddressToDto(addressModel)).value

        FX.getComponents(Scope())
                .put(PersonalInvoiceDetailsWizardView::class,customerModel)
        isComplete = customerModel.commit(customerModel.name)
    }

    override val root= vbox{
        prefWidth=950.0
        prefHeight=700.0

        label("Customer details")
        form {
            style { stylesheets.add("styles.css") }
            fieldset {
                field("First name") {
                    textfield(customerModel.name) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Last name") {
                    textfield(customerModel.surname) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("E-mail") {
                    textfield(customerModel.email) {
                        style { id = "text-field" }
                        required()
                    }
                }

                field("Phone number") {
                    textfield(customerModel.phoneNumber) {
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
        label("Customer address details")
        form {
            style { stylesheets.add("styles.css") }
            fieldset {
                field("Country") {
                    textfield(addressModel.country) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("City") {
                    textfield(addressModel.city) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Street") {
                    textfield(addressModel.street) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Building") {
                    textfield(addressModel.building) {
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
    }
}