package com.nowak.demo.view

import com.example.demo.app.Styles
import com.nowak.demo.controllers.LoginController
import com.nowak.demo.database.InvoicerDatabase

import com.nowak.demo.models.login.User
import com.nowak.demo.models.login.UserModel
import  de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.USER
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.input.KeyCode

import tornadofx.*
import java.time.LocalDate
import javax.inject.Inject


class LoginView : View() {

    var userModel = UserModel()
    val loginController: LoginController by inject()

    init {
    }

    override val root = borderpane {
        this.id = "scene-bg"
        this.stylesheets.add("styles.css")

        top {
            label(" Invoicer Manager") {
                alignment = Pos.TOP_CENTER
                style {
                    id = "logo-label"
                    stylesheets.add("styles.css")
                }
            }
        }
        center = form {
            addClass(Styles.heading)
            label {
                text = "Login"
                alignment = Pos.CENTER
            }
            fieldset() {
                field("Username") {
                    textfield(userModel.username) {
                        promptText = "Username"
                        required()
                        requestFocus()
                        validator {
                            when {
                                it.isNullOrEmpty() || it.isNullOrBlank()
                                -> error(" Username cannot be empty")

                                else
                                -> null
                            }
                        }
                        style {
                            id = "text-field"
                            stylesheets.add("styles.css")
                        }
                    }
                }
                field("Password") {
                    passwordfield(userModel.password) {
                        promptText = "Password"
                        required()
                        validator {
                            when {
                                it.isNullOrEmpty() || it.isNullOrBlank()
                                -> error("Password cannot be empty")
                                else
                                -> null
                            }
                        }
                        style {
                            id = "text-field"
                            stylesheets.add("styles.css")
                        }

                        setOnKeyPressed {
                            action {
                                if (loginController.login(userModel.username.value!!,
                                                userModel.password.value!!)) {
                                    loggedUser = loginController.getLoggedUser(userModel.username.value)
                                    userModel.rollback()
                                    replaceWith(WorkspaceView::class,
                                            ViewTransition.Slide(0.5.seconds,
                                                    ViewTransition.Direction.LEFT))
                                } else {
                                    error("Invalid username or password")
                                }
                            }
                        }
                    }
                }
            }
            vbox {
                addClass(Styles.vbox)
                button {
                    text = "Login"
                    style {
                        id = "login-button"
                        stylesheets.add("styles.css")
                    }
                    enableWhen(userModel.valid)
                    action {
                        if (loginController.login(userModel.username.value!!,
                                        userModel.password.value!!)) {
                                loggedUser = loginController.getLoggedUser(userModel.username.value)
                                userModel.rollback()
                                replaceWith(WorkspaceView::class,
                                        ViewTransition.Slide(0.5.seconds,
                                                ViewTransition.Direction.LEFT))

                            } else {
                            error("Invalid username or password")
                        }
                    }
                }

                button("Register") {
                    this.id = "login-button"
                    this.stylesheets.add("styles.css")
                    action {
                        replaceWith(RegisterView::class,
                                ViewTransition.Slide(0.5.seconds,
                                        ViewTransition.Direction.LEFT))
                        userModel.rollback()
                    }
                }.apply {

                }
            }
        }
    }

}

