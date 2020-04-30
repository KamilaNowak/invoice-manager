package com.example.demo.app

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val vbox by cssclass()
    }

    init {
        label and heading {
            padding = box(15.px)
            fontSize = 40.px
            fontWeight = FontWeight.BOLD
        }
        vbox{
            padding=box(15.px)
            spacing=15.px
            fontSize=18.px
        }

        form{
            padding= box(12.px)
        }
    }
}