package com.nowak.demo.view

import com.nowak.demo.controllers.UserController
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class EditEmail :  View("Change email") {

    private val loggedEmail = SimpleStringProperty()
    private val userController: UserController by inject()
    private val status = SimpleStringProperty("")

    override val root = vbox {
        this.stylesheets.add("styles.css")
        style { id = "modal" }
        label("Type your new email") { style { fontSize = 20.px } }
        textfield(loggedEmail) {
            style { id = "text-field" }
        }
        button("Submit") {
            enableWhen(loggedEmail.isNotBlank())
            style { id = "dashboard-button" }
            action {
                if(!userController.checkUsernameEmailAvailability(email = loggedEmail.value)) {
                    if (userController.updateUser(userId = loggedUser, newEmail = loggedEmail.value))
                        status.value = "Successfully updated"
                    else status.value = "Something went wrong"
                }
                else status.value="Email is taken. Try another one "
            }
        }
        label().bind(status)
    }
}
