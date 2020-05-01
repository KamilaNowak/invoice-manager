package com.nowak.demo.view

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import tornadofx.*
import tornadofx.FX.Companion.stylesheets

var loggedUser: Long = 0

class WorkspaceView : Workspace("Invoice Manager", NavigationMode.Tabs) {
    init {
        stylesheets.add("styles.css")

        button("Account") {
            graphic = FontAwesomeIconView(FontAwesomeIcon.USER)
            style { id = "workspace-button" }
            action {
                replaceWith(AccountView::class,
                        ViewTransition.Slide(0.5.seconds,
                                ViewTransition.Direction.DOWN))
            }
        }
        button("Log out") {
            graphic = FontAwesomeIconView(FontAwesomeIcon.USER_TIMES)
            style { id = "workspace-button" }
            action {
                replaceWith(LoginView::class,
                        ViewTransition.Metro(0.5.seconds,
                                ViewTransition.Direction.LEFT))
            }
        }

        dock<StatisticsView>()
        dock<InvoicesView>()

    }
}