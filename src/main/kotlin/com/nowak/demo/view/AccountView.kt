package com.nowak.demo.view

import com.nowak.demo.controllers.UserController
import com.nowak.demo.database.InvoicerDatabase
import com.nowak.demo.models.login.User
import com.nowak.demo.models.login.UserModel
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import tornadofx.*
import java.time.LocalDate

class AccountView : View() {

    val loggedEmail = SimpleStringProperty()
    val loggedBirthDate = SimpleObjectProperty<LocalDate>()
    val newPassword = SimpleStringProperty()
    val invoicerDatabase = InvoicerDatabase()

    val userController: UserController by inject()
    var user: User

    init {
        user = userController.findUserById(loggedUser)
    }

    override val root = vbox {
        style {
            id = "second-scene-bg"
            stylesheets.add("styles.css")
            paddingAll = 15.0
        }
        button {
            text = "Go back"
            action {
                replaceWith(WorkspaceView::class,
                        ViewTransition.Slide(0.5.seconds,
                                ViewTransition.Direction.UP))
            }
            style {
                id = "dashboard-button"
                stylesheets.add("styles.css")
                paddingBottom = 20.0
            }
        }
        label {
            text = "Account details"
            style { fontSize = 20.px }
        }
        vbox {
            spacing = 15.0

            hbox {
                spacing = 10.0
                label("Username") {
                    style { fontSize = 17.px }
                }
                textfield(user.username) {
                    isDisable = true
                    style {
                        id = "text-field"
                        stylesheets.add("styles.css")
                    }
                }
                button("Change") {
                    style {
                        id = "dashboard-button"
                        stylesheets.add("styles.css")
                    }
                    action { openInternalWindow<EditUsername>() }
                }
            }
            hbox {
                spacing = 10.0
                label("Email") {
                    style { fontSize = 17.px }
                }
                textfield(user.email) {
                    isDisable = true
                    style {
                        id = "text-field"
                        stylesheets.add("styles.css")
                    }
                }
                button {
                    text = "Change"
                    style {
                        id = "dashboard-button"
                        stylesheets.add("styles.css")
                    }
                    action {

                    }
                }
            }
            hbox {
                spacing = 10.0
                label("Birth date") {
                    style { fontSize = 17.px }
                }
                textfield(user.birthDate.toString()) {
                    isDisable = true
                    style {
                        id = "text-field"
                        stylesheets.add("styles.css")
                    }
                }
                button("Change") {
                    style {
                        id = "dashboard-button"
                        stylesheets.add("styles.css")
                    }
                }
            }
            hbox {
                spacing = 10.0
                label("Registration date") {
                    style { fontSize = 17.px }
                }
                textfield(user.createdAt.toString()) {
                    isDisable = true
                    style {
                        id = "text-field"
                        stylesheets.add("styles.css")
                    }
                }
            }
            hbox {
                spacing = 10.0
                label("Client ID") {
                    style { fontSize = 17.px }
                }
                textfield("#${user.id}") {
                    isDisable = true
                    style {
                        id = "text-field"
                        stylesheets.add("styles.css")
                    }
                }
            }
            label { paddingAll = 10.0 }
            hbox {
                spacing = 10.0
                label("Secure your account") {
                    style { fontSize = 17.px }
                }
                button {
                    text = "Change password"
                    style {
                        id = "dashboard-button"
                        stylesheets.add("styles.css")
                    }
                }
            }
        }

    }
}


