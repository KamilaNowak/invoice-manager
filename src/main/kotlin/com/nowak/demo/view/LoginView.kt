package com.nowak.demo.view

import com.example.demo.app.Styles
import com.nowak.demo.controllers.LoginController
import com.nowak.demo.models.login.UserModel
import javafx.geometry.Pos
import tornadofx.*


class LoginView : View() {

    private var userModel = UserModel()
    private val loginController: LoginController by inject()

    override val root = vbox {
        this.id = "scene-bg"
        this.stylesheets.add("styles.css")
        addClass(Styles.vbox)

        label(" Invoice Manager") {
            alignment = Pos.TOP_CENTER
            style { id = "logo-label" }
        }
        label("Login") { id = "info-label" }

        form {
            addClass(Styles.heading)
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
                                else -> null
                            }
                        }
                        style { id = "text-field" }
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
                                else -> null
                            }
                        }
                        style { id = "text-field" }

                        setOnKeyPressed {
                            action {
                                if (loginController.login(userModel.username.value!!,
                                                userModel.password.value!!)) {
                                    loggedUser = loginController.getLoggedUser(userModel.username.value)
                                    userModel.rollback()
                                    replaceWith(WorkspaceView::class,
                                            ViewTransition.Slide(0.5.seconds,
                                                    ViewTransition.Direction.LEFT))
                                } else error("Invalid username or password")
                            }
                        }
                    }
                }
            }
            vbox {
                addClass(Styles.vbox)
                button("Login") {
                    style { id = "login-button" }
                    enableWhen(userModel.valid)
                    action {
                        if (loginController.login(userModel.username.value!!,
                                        userModel.password.value!!)) {
                            loggedUser = loginController.getLoggedUser(userModel.username.value)
                            userModel.rollback()
                            replaceWith(WorkspaceView::class,
                                    ViewTransition.Slide(0.5.seconds,
                                            ViewTransition.Direction.LEFT))

                        } else error("Invalid username or password")
                    }
                }

                button("Register") {
                    action {
                        replaceWith(RegisterView::class,
                                ViewTransition.Slide(0.5.seconds,
                                        ViewTransition.Direction.LEFT))
                        userModel.rollback()
                    }
                    style { id = "login-button" }
                }
            }
        }
    }

}

