package com.example.demo.app

import com.nowak.demo.view.LoginView
import javafx.stage.Stage
import tornadofx.*

class InvoicerManagerApp: App(LoginView::class, Styles::class){
    override fun start(stage: Stage) {
        with(stage){
            width=600.0
            height=800.0
            isResizable=true
        }
        super.start(stage)
    }
    init {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
    }
}