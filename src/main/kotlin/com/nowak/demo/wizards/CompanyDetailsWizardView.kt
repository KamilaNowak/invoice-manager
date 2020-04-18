package com.nowak.demo.wizards

import com.nowak.demo.models.addresses.AddressDetails
import com.nowak.demo.models.addresses.AddressDetailsModel
import com.nowak.demo.models.customers.CompanyModel
import com.nowak.demo.models.customers.Owner
import com.nowak.demo.models.customers.OwnerModel
import javafx.beans.binding.BooleanExpression
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import sun.java2d.pipe.SpanShapeRenderer
import tornadofx.*

class CompanyDetailsWizardView : View("Owner informations") {

    private val ownerModel = OwnerModel()
    private val addressModel = AddressDetailsModel()
    private val companyModel = CompanyModel()


    override fun onSave() {
        companyModel.owner.value=SimpleObjectProperty<Owner>(
                Owner(0,ownerModel.name.value, ownerModel.surname.value, ownerModel.email.value, ownerModel.pid.value.toInt())).value
        companyModel.address.value =  SimpleObjectProperty<AddressDetails>(
                AddressDetails(0,addressModel.country.value, addressModel.city.value, addressModel.street.value, addressModel.building.value.toInt())).value
       //isComplete = companyModel.commit(companyModel.companyName, companyModel.nip)

        val scope =Scope()
        setInScope(companyModel, scope)
        this+=find<InvoiceDetailsWizardView>(scope)
    }
    override val root = vbox {
        label("Owner details")
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
                field("PID") {
                    textfield(ownerModel.pid) {
                        style { id = "text-field" }
                        required()
                    }
                }
            }
        }
        label("Address details")
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
            style{ stylesheets.add("styles.css") }
            fieldset {
                field("Company name") {
                    textfield(companyModel.companyName) {
                        style { id = "text-field" }
                        required()
                    }
                }
                field("Tax number"){
                    textfield(companyModel.nip) {
                        style {id="text-field"}
                        required()
                    }
                }
            }
        }
    }

}