package com.nowak.demo.controllers

import com.nowak.demo.database.CountDates
import com.nowak.demo.database.InvoicesDatabase
import com.nowak.demo.database.InvoicesShowcaseDatabase
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.models.summary.InvoiceSummaryModel
import javafx.collections.ObservableList
import tornadofx.*
import kotlin.collections.ArrayList

class InvoicesShowcaseController : Controller() {
    private val invoicesShowcaseDatabase = InvoicesShowcaseDatabase()
    private val invoicesDatabase = InvoicesDatabase()

    fun getInvoiceStatistic(receiverType: ReceiverType): ArrayList<CountDates> {
        return invoicesShowcaseDatabase.getCountDateCompanyInvoices(receiverType)
    }

    fun getTotalPreparedInvoices(): Int {
        return invoicesShowcaseDatabase.countTotalPreparedInvoices()
    }

    fun getCompanyInvoiceSummary(): ObservableList<InvoiceSummaryModel> {
        val list: ArrayList<InvoiceSummaryModel> = arrayListOf()
        val items = invoicesDatabase.getCompanyInvoiceSummary()

        items.forEach {
            list.add(InvoiceSummaryModel().apply {
                item = it
            })
        }
        return list.observable()
    }
    fun getPersonalInvoiceSummary(): ObservableList<InvoiceSummaryModel> {
        val list: ArrayList<InvoiceSummaryModel> = arrayListOf()
        val items2 = invoicesDatabase.getPersonalInvoiceSummary()
        items2.forEach {
            list.add(InvoiceSummaryModel().apply {
                item = it
            })
        }
        return list.observable()
    }
}