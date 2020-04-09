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
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        vbox{
            padding=box(12.px)
            spacing=10.px
          //  backgroundColor = multi(c("#E0E0E0"))
        }

        form{
            padding= box(12.px)
        }
    }
}