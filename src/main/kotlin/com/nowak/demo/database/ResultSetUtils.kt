package com.nowak.demo.database

import com.nowak.demo.models.addresses.AddressDetails
import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.customers.Owner
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.invoices.PaymentMethod
import com.nowak.demo.models.invoices.PersonalInvoice
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.items.ItemCategory
import com.nowak.demo.models.summary.InvoiceSummary
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.ZoneId

val invoicesDatabase = InvoicesDatabase()

fun generateInvoiceNo(): String {
    val prefix = "INV"
    val date = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return prefix + date.toString()
}

fun getOwnerFromResultSet(resultSet: ResultSet): Owner {
    return Owner(resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("surname"),
            resultSet.getString("email"),
            resultSet.getLong("phone_number"),
            resultSet.getInt("pid"))
}

fun getAddressFromResultSet(resultSet: ResultSet): AddressDetails {
    return AddressDetails(resultSet.getLong("id"),
            resultSet.getString("country"),
            resultSet.getString("city"),
            resultSet.getString("street"),
            resultSet.getInt("building"))
}

fun getCompanyFromResultSet(resultSet: ResultSet): Company {
    return Company(resultSet.getLong("id"),
            resultSet.getString("company_name"),
            resultSet.getLong("nip"),
            invoicesDatabase.findAddressDetailsById(resultSet.getInt("address_id"))!!,
            invoicesDatabase.findOwnerById(resultSet.getInt("owner_id").toLong())!!)
}

fun getCustomerFromResultSet(resultSet: ResultSet): Customer {
    return Customer(resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("surname"),
            resultSet.getString("email"),
            resultSet.getLong("phone_number"),
            invoicesDatabase.findAddressDetailsById(resultSet.getInt("address_id"))!!)
}

fun getCompanyInvoiceFromResultSet(resultSet: ResultSet): CompanyInvoice {
    return CompanyInvoice(resultSet.getString("invoice_no"),
            resultSet.getDate("date_of_issue").toLocalDate(),
            resultSet.getLong("amount"),
            PaymentMethod.valueOf(resultSet.getString("payment_option")),
            invoicesDatabase.findCompanyById(resultSet.getLong("company_id"))!!,
            userDatabase.findUserById(resultSet.getLong("prepared_by")))
}

fun getPersonalInvoiceFromResultSet(resultSet: ResultSet): PersonalInvoice {
    return PersonalInvoice(resultSet.getString("invoice_no"),
            resultSet.getDate("date_of_issue").toLocalDate(),
            resultSet.getLong("amount"),
            PaymentMethod.valueOf(resultSet.getString("payment_option")),
            resultSet.getInt("discount"),
            invoicesDatabase.findCustomerById(resultSet.getLong("customer_id"))!!,
            userDatabase.findUserById(resultSet.getLong("prepared_by")))
}

fun getItemFroResultSet(resultSet: ResultSet): Item {
    return Item(resultSet.getLong("id"),
            resultSet.getString("description"),
            resultSet.getLong("cost"),
            resultSet.getLong("quantity"),
            resultSet.getInt("vat"),
            ItemCategory.valueOf((resultSet.getString("category"))),
            resultSet.getString("invoice_no"))
}

fun getSummaryFromResultSet(resultSet: ResultSet): InvoiceSummary {
    return InvoiceSummary(resultSet.getString("invoice_no"),
            resultSet.getDate("date_of_issue").toLocalDate(),
            resultSet.getInt("amount"),
            resultSet.getString("receiver"),
            resultSet.getString("pdf_link"))
}