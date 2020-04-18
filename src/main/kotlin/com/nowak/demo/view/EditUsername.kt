package com.nowak.demo.view

import com.nowak.demo.controllers.UserController
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class EditUsername : View("Change username") {

    private val loggedUsername = SimpleStringProperty()
    private val userController: UserController by inject()
    private val status = SimpleStringProperty("")

    override val root = vbox {
        this.stylesheets.add("styles.css")
        style { id = "modal" }
        label("Type your new username") { style { fontSize = 20.px } }
        textfield(loggedUsername) {
            style { id = "text-field" }
        }
        button("Submit") {
            enableWhen(loggedUsername.isNotBlank())
            style { id = "dashboard-button" }
            action {
                if(!userController.checkUsernameEmailAvailability(username = loggedUsername.value)) {
                    if (userController.updateUser(userId = loggedUser, newUsername = loggedUsername.value))
                        status.value = "Successfully updated"
                    else status.value = "Something went wrong"
                }
                else status.value="Username is taken. Try another one "
            }
        }
        label().bind(status)
    }
}