package com.nowak.demo.database

import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.view.loggedUser
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate

class InvoicesShowcaseDatabase {

    private lateinit var resultSet: ResultSet

    fun countTotalPreparedInvoices(): Int {
            resultSet= DatabaseQueryUtils.
                    createQuery("SELECT (SELECT COUNT(invoice_no) FROM company_invoices WHERE prepared_by= ?) + " +
                            "(SELECT COUNT(invoice_no) FROM personal_invoices WHERE prepared_by= ?) AS total;", mapOf(1 to getLoggedUser().id, 2 to getLoggedUser().id))
            resultSet.next()
        return resultSet.getInt("total")
    }

    fun getCountDateCompanyInvoices(receiverType: ReceiverType): ArrayList<CountDates> {
        val list: ArrayList<CountDates> = arrayListOf()
        val query: String = if (receiverType == ReceiverType.PERSON)
            "SELECT COUNT(date_of_issue) AS date_count, date_of_issue " +
                    "FROM personal_invoices " +
                    "GROUP BY date_of_issue " +
                    "ORDER BY date_of_issue"
        else
            "SELECT DISTINCT COUNT(date_of_issue) OVER (PARTITION BY date_of_issue) AS date_count, date_of_issue " +
                    "FROM company_invoices " +
                    "ORDER BY date_of_issue"
        try {
            resultSet = DatabaseQueryUtils
                    .createQuery(query, emptyMap())
            if (resultSet.next()) {
                do {
                    val date = resultSet.getDate("date_of_issue").toLocalDate()
                    val count = resultSet.getInt("date_count")
                    list.add(CountDates(count, date))

                } while (resultSet.next())
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
    }
}

class CountDates(val key: Int, val value: LocalDate)