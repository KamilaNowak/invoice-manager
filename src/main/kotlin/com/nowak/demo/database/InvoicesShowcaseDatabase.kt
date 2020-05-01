package com.nowak.demo.database

import com.nowak.demo.models.invoices.PaymentMethod
import com.nowak.demo.models.items.ItemCategory
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.view.loggedUser
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import javax.xml.crypto.Data

class InvoicesShowcaseDatabase {

    private lateinit var resultSet: ResultSet

    fun countTotalPreparedInvoices(): Int {
        resultSet = DatabaseQueryUtils.createQuery("SELECT (SELECT COUNT(invoice_no) FROM company_invoices WHERE prepared_by= ?) + " +
                "(SELECT COUNT(invoice_no) FROM personal_invoices WHERE prepared_by= ?) AS total;", mapOf(1 to getLoggedUser().id, 2 to getLoggedUser().id))
        resultSet.next()
        return resultSet.getInt("total")
    }

    fun getCountPayment(): ArrayList<CountPayment> {
        val list: ArrayList<CountPayment> = arrayListOf()
        try {
            resultSet = DatabaseQueryUtils
                    .createQuery("SELECT COUNT(payment_option), payment_option FROM (SELECT payment_option FROM company_invoices UNION ALL SELECT payment_option FROM personal_invoices) AS count GROUP BY payment_option", emptyMap())
            if (resultSet.next()) {
                do {
                    val count = resultSet.getInt("count")
                    val paymentMethod = PaymentMethod.valueOf(resultSet.getString("payment_option"))
                    list.add(CountPayment(count, paymentMethod))

                } while (resultSet.next())
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
    }

    fun getCountCategory(): ArrayList<CountCategory> {
        val list: ArrayList<CountCategory> = arrayListOf()
        try{
            resultSet =DatabaseQueryUtils
                    .createQuery("SELECT DISTINCT COUNT(id) OVER(PARTITION BY category), category FROM items", emptyMap())
            if(resultSet.next()){
                do{
                    val count = resultSet.getInt("count")
                    val category = ItemCategory.valueOf(resultSet.getString("category"))
                    list.add(CountCategory(count,category))
                }while(resultSet.next())
            }
        }catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
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
class CountPayment(val count: Int, val payment: PaymentMethod)
class CountCategory(val count: Int, val category: ItemCategory)