package com.nowak.demo.view

import com.nowak.demo.controllers.UserController
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate

class EditBirthDate : View("Change birth date") {

    private val loggedBirthDate = SimpleObjectProperty<LocalDate>()
    private val userController: UserController by inject()
    private val status = SimpleStringProperty("")

    override val root = vbox {
        this.stylesheets.add("styles.css")
        style { id = "modal" }
        label("Chose your new birth date") { style { fontSize = 20.px } }
        datepicker(loggedBirthDate) {
            style { id = "text-field" }
        }
        button("Submit") {
            enableWhen(loggedBirthDate.isNotNull)
            style { id = "dashboard-button" }
            action {
                if (userController.updateUser(userId = loggedUser, newBirthDate = loggedBirthDate.value))
                    status.value = "Successfully updated"
                else status.value = "Something went wrong"
            }
        }
        label().bind(status)
    }
}