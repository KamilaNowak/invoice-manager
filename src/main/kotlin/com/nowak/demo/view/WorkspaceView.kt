package com.nowak.demo.view

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.application.Platform
import tornadofx.*

var loggedUser: Long = 0

class WorkspaceView : Workspace("Invoicer", NavigationMode.Tabs) {
    init {
        button {
            graphic = FontAwesomeIconView(FontAwesomeIcon.USER)
            text = "Account"
            style {
                id = "workspace-button"
                stylesheets.add("styles.css")
            }
            action {
                replaceWith(AccountView::class,
                        ViewTransition.Slide(0.5.seconds,
                                ViewTransition.Direction.DOWN))
            }
        }
        button {
            text = "Log out"
            graphic = FontAwesomeIconView(FontAwesomeIcon.USER_TIMES)
            style {
                id = "workspace-button"
                stylesheets.add("styles.css")
            }
            action {
                replaceWith(LoginView::class,
                        ViewTransition.Metro(0.5.seconds,
                                ViewTransition.Direction.LEFT))
            }
        }
        dock<CompanyInvoicesView>()
        dock<StatisticsView>()
    }
}