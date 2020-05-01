package com.example.demo.app

import com.nowak.demo.view.LoginView
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*
import java.io.File





class InvoicerManagerApp : App(LoginView::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 1080.0
            height = 930.0
            isResizable = true
        }
        stage.icons.add(Image("static/invoice-logo.jpg"))
        stage.title = "Invoice Manager"

        super.start(stage)
    }

    init {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
        Runtime.getRuntime().exec("cmd /c start .\\src\\main\\resources\\clean.bat");
    }
}
