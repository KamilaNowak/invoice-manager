package com.nowak.demo.view

import com.nowak.demo.controllers.UserController
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class EditPassword : View("Change password") {

    private val newPassword = SimpleStringProperty()
    private val oldPassword = SimpleStringProperty()
    private val userController: UserController by inject()
    private val status = SimpleStringProperty("")

    override val root = vbox {
        this.stylesheets.add("styles.css")
        style { id = "modal" }
        label("Change your actual password") { style { fontSize = 20.px } }
        passwordfield(oldPassword) {
            label("Old password").apply { id="password-label"}
            style { id = "text-field" }
        }
        passwordfield(newPassword) {
            label("New password").apply { id="password-label"}
            style { id = "text-field" }
        }
        button("Submit") {
            enableWhen(oldPassword.isNotBlank())
            style { id = "dashboard-button" }
            action {
                if (userController.confirmUser(oldPassword.value)) {
                    if (userController.updateUser(userId = loggedUser, newPassword = newPassword.value))
                        status.value = "Successfully changed"
                    else status.value = "Something went wrong"
                }
                else status.value="Old password is incorrect"
            }
        }
        label().bind(status)
    }
}
