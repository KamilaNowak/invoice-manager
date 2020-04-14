package com.nowak.demo.view

import com.nowak.demo.controllers.UserController
import com.nowak.demo.database.InvoicerDatabase
import com.nowak.demo.models.login.User
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos

import tornadofx.*
import java.time.LocalDate

class AccountView : View() {

    val userController: UserController by inject()
    var user: User

    init {
        user = userController.findUserById(loggedUser)
    }

    override val root = vbox {
        this.stylesheets.add("styles.css")
        style {
            id = "second-scene-bg"
            paddingAll = 15.0
        }
        button {
            text = "Go back"
            graphic = FontAwesomeIconView(FontAwesomeIcon.CHEVRON_LEFT)
            action {
                replaceWith(WorkspaceView::class,
                        ViewTransition.Slide(0.5.seconds,
                                ViewTransition.Direction.UP))
            }
            style {
                id = "dashboard-button"
                paddingBottom = 20.0
            }
        }
        label { paddingAll = 5.0 }
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
                    }
                }
                button("Change") {
                    style {
                        id = "dashboard-button"
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
                    style { id = "text-field" }
                }
                button("Change") {
                    style { id = "dashboard-button" }
                    action {
                        openInternalWindow<EditEmail>()
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
                    style { id = "text-field" }
                }
                button("Change") {
                    style {id = "dashboard-button" }
                    action{
                        openInternalWindow<EditBirthDate>()
                    }
                }
            }
            hbox {
                spacing = 10.0
                label("Registration date") { style { fontSize = 17.px } }
                textfield(user.createdAt.toString()) {
                    isDisable = true
                    style { id = "text-field" }
                }
            }
            hbox {
                spacing = 10.0
                label("Client ID") { style { fontSize = 17.px } }
                textfield("#${user.id}") {
                    isDisable = true
                    style { id = "text-field" }
                }
            }
            label { paddingAll = 10.0 }
            hbox {
                spacing = 10.0
                label("Secure your account") { style { fontSize = 17.px } }
                button {
                    text = "Change password"
                    style {id = "dashboard-button"}
                    action{
                        openInternalWindow<EditPassword>()
                    }
                }
            }
        }
    }
}


