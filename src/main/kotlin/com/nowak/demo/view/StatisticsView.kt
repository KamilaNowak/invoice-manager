package com.nowak.demo.view

import com.nowak.demo.controllers.InvoicesShowcaseController
import com.nowak.demo.database.CountDates
import com.nowak.demo.models.items.ReceiverType
import javafx.scene.Parent
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.PieChart
import javafx.scene.paint.Color
import tornadofx.*
import java.time.LocalDate

class StatisticsView : View("Statistics") {
    private val showcaseController: InvoicesShowcaseController by inject()
    private var dataCompany: ArrayList<CountDates>
    private var dataPersonal: ArrayList<CountDates>

    init {
        dataCompany = showcaseController.getInvoiceStatistic(ReceiverType.COMPANY)
        dataPersonal = showcaseController.getInvoiceStatistic(ReceiverType.PERSON)
    }

    override val root = vbox {
        stylesheets.add("styles.css")
        this.style { id = "third-scene-bg" }
        vbox {
            id = "vbox-organized"
            textfield {
                text = "You have already prepared " +
                        showcaseController.getTotalPreparedInvoices().apply { id = "status-label" } + " invoices"
                isDisable = true
            }
            barchart("Invoices statistics depend on date", CategoryAxis(), NumberAxis()) {
                series("For Companies") {
                    dataCompany.forEach {
                        data(it.value.toString(), it.key)
                    }
                }
                series("For independent customers"){
                    dataPersonal.forEach {
                        data(it.value.toString(), it.key)
                    }
                }
            }
        }
        hbox {
            style{
             id="#vbox-organized"
            }
            piechart {
                title="Most popular categories"
                for(item in showcaseController.getCountCategorySummary()){
                    data.add(PieChart.Data(item.category.toString(), item.count.toDouble()))
                }
            }

            piechart {
                title="Most popular payment methods"
                for(item in showcaseController.getCountPaymentOptionSummary()){
                    data.add(PieChart.Data(item.payment.toString(), item.count.toDouble()))
                }
            }
        }
    }
}