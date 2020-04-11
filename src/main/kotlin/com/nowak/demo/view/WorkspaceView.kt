package com.nowak.demo.view

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import tornadofx.*

class WorkspaceView : Workspace("Invoicer", NavigationMode.Tabs){
    init{
        button("Log out") {
            graphic = FontAwesomeIconView(FontAwesomeIcon.USER_TIMES)
            this.id = "logout-button"
            this.stylesheets.add("styles.css")
            action{
                replaceWith(LoginView::class,
                        ViewTransition.Metro(0.5.seconds,
                                ViewTransition.Direction.LEFT))
            }
        }
        dock<DashboradView>()
    }
}