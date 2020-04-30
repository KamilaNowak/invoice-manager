package com.nowak.demo.wizards.company

import com.nowak.demo.models.addresses.AddressDetails
import com.nowak.demo.models.addresses.AddressDetailsModel
import com.nowak.demo.models.customers.CompanyModel
import com.nowak.demo.models.customers.Owner
import com.nowak.demo.models.customers.OwnerModel
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class CompanyDetailsWizardView : View("Owner informations") {

    private val ownerModel: OwnerModel by inject()
    private val addressModel: AddressDetailsModel by inject()
    private val companyModel: CompanyModel by inject()

    override fun onSave() {
        companyModel.owner.value =
                SimpleObjectProperty(OwnerModel.convertOwnerModelToDto(ownerModel)).value
        companyModel.address.value =
                SimpleObjectProperty(AddressDetailsModel.convertAddressToDto(addressModel)).value

        FX.getComponents(Scope())
                .put(InvoiceDetailsWizardView::class, companyModel)

        isComplete = companyModel.commit(companyModel.companyName)
    }

    override val root = vbox {
        prefWidth=950.0
        prefHeight=700.0

        label("Company owner details")
        form {
            style { stylesheets.add("styles.css") }
            fieldset {
                field("First name") {
                    textfield(ownerModel.name) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Last name") {
                    textfield(ownerModel.surname) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("E-mail") {
                    textfield(ownerModel.email) {
                        style { id = "text-field" }
                        required()
                    }
                }

                field("Phone number") {
                    textfield(ownerModel.phoneNumber) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("PID") {
                    textfield(ownerModel.pid) {
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
        label("Company address details")
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
        label("Company details")
        form {
            style { stylesheets.add("styles.css") }
            fieldset {
                field("Company name") {
                    textfield(companyModel.companyName) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Tax number") {
                    textfield(companyModel.nip) {
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
    }
}