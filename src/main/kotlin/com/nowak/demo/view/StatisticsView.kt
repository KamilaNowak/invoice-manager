package com.nowak.demo.view

import javafx.scene.Parent
import tornadofx.*

class StatisticsView : View("Statistics"){
    override val root = vbox{
        stylesheets.add("styles.css")
        style{ id="third-scene-bg" }
        label("to implement")
    }
}