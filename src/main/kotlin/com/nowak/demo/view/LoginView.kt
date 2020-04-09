package com.nowak.demo.view

import com.example.demo.app.Styles
import com.nowak.demo.models.login.UserModel
import  de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.USER
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.input.KeyCode
import tornadofx.*


class LoginView : View("Login") {

    val userModel = UserModel()

    override val root = borderpane {
        top = label(" Invoicer Manager") {
            addClass(Styles.heading)
        }

        center = form {
            addClass(Styles.heading)
            label {
                text = "Login"
            }
            fieldset("", FontAwesomeIconView(USER)) {
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
                        setOnKeyPressed {
                            if (it.code == KeyCode.ENTER) {
                                userModel.commit() {
                                    userModel.rollback()
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
                    enableWhen(userModel.valid)
                    action {
                        userModel.commit() {

                            userModel.rollback()
                        }
                    }
                }

                button("Register") {
                    action {
                        replaceWith(RegisterView::class,
                                ViewTransition.Slide(0.5.seconds,
                                        ViewTransition.Direction.LEFT))
                    }

                }.apply {

                }
            }
        }
    }
}