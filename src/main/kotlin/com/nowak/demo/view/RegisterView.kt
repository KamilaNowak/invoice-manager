package com.nowak.demo.view

import com.example.demo.app.Styles
import com.nowak.demo.controllers.LoginController
import com.nowak.demo.controllers.RegisterController
import com.nowak.demo.models.login.User
import com.nowak.demo.models.login.UserModel
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import tornadofx.*
import java.util.regex.Pattern

class RegisterView : View() {
    val URL_PRIVACY_POLICY = "https://idpc.org.mt/en/Documents/Sample%20Privacy%20Policy.pdf"
    val userModel = UserModel()
    val privacyPolicyCheckbox = SimpleBooleanProperty()
    val registerController: RegisterController by inject()

    override val root = vbox {
        this.id="scene-bg"
        this.stylesheets.add("styles.css")
        label {
            text = "Invoicer Manager"
            alignment = Pos.TOP_CENTER
            this.id = "logo-label"
            this.stylesheets.add("styles.css")

        }
        addClass(Styles.vbox)

        label {
            text = "Register"
        }

        form {
            fieldset(labelPosition = Orientation.HORIZONTAL) {
                field("Username") {
                    textfield(userModel.username) {
                        required()
                        promptText = "Username"
                        validator {
                            when{
                                it.isNullOrBlank() -> error("Username cannot be blank")
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
                field("E-mail") {
                    textfield(userModel.email) {
                        required()
                        promptText = "E-mail"
                        validator {
                            when {
                                it.isNullOrEmpty() || it.isNullOrBlank()
                                -> error("Email cannot be empty")

                                !checkIfEmailIsValid(it) -> error("E-mail is not valid")

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
                        required()
                        promptText = "Password"
                        validator {
                            when {
                                it.isNullOrEmpty() || it.isNullOrBlank()
                                -> error("Password cannot be empty")

                                it!!.length < 3 -> error("Password must be at least 4 charactes")
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
                field("Birth date") {
                    datepicker(userModel.birthDate) {
                        required()
                        validator {
                            when {
                                it?.dayOfMonth.toString().isEmpty() ||
                                        it?.dayOfWeek.toString().isEmpty() ||
                                        it?.dayOfYear.toString().isEmpty()
                                -> error("Birth date cannot be empty")
                                else
                                -> null
                            }
                        }
                        style {
                            id = "text-field"
                            stylesheets.add("styles.css")
                        }
                        setOnKeyPressed {
                            if (it.code == KeyCode.ENTER) {
                                if (userModel.isValid) {
                                    userModel.commit {
                                        userModel.rollback()
                                    }
                                }
                            }
                        }
                    }
                }
                checkbox("", privacyPolicyCheckbox) {
                    hyperlink(" Agree with Privacy Policy")
                            .action {
                                hostServices.showDocument(URL_PRIVACY_POLICY)
                            }
                    require(true)
                }
                vbox {
                    addClass(Styles.vbox)
                    button {
                        text = "Register"
                        style {
                            id = "login-button"
                            stylesheets.add("styles.css")
                        }
                        enableWhen {
                            privacyPolicyCheckbox
                            userModel.valid
                        }
                        action {
                            if (registerController.register(
                                            userModel.username.value!!,
                                            userModel.password.value!!,
                                            userModel.email.value!!,
                                            userModel.birthDate.value!!)) {
                                information("User registered successfully", "Now you are able to log in")
                                replaceWith(LoginView::class,
                                        ViewTransition.Swap(0.5.seconds,
                                                ViewTransition.Direction.RIGHT))
                            } else {
                                error("Username or email is taken. Try with another ones.")
                            }
                        }
                    }

                    button {
                        text = "Go back to login"
                        style {
                            id = "login-button"
                            stylesheets.add("styles.css")
                        }
                        action {
                            userModel.rollback()
                            replaceWith(LoginView::class,
                                    ViewTransition.Cover(0.5.seconds,
                                            ViewTransition.Direction.RIGHT))
                        }
                    }
                }
            }
        }
    }

}

fun checkIfEmailIsValid(email: String?): Boolean {
    val regex = "^[A-Za-z0-9+_.-]+@(.+)$"
    val pattern = Pattern.compile(regex)
    return pattern.matcher(email).matches()
}