package com.nowak.demo.database

import com.nowak.demo.aws.S3Uploader
import com.nowak.demo.models.addresses.AddressDetails
import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.customers.Owner
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.invoices.PersonalInvoice
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.models.summary.InvoiceSummary
import com.nowak.demo.pdf.PDFGenerator
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.*
import java.sql.ResultSet
import java.sql.SQLException
import java.util.stream.Collectors

class InvoicesDatabase {

    private val resultSetProperty = SimpleObjectProperty<ResultSet>()
    private var resultSet by resultSetProperty
    private val s3 = S3Uploader()

    fun getEmailByInvoiceNo(invoiceNo: String, receiver: ReceiverType): String? {
        val params = mapOf(1 to invoiceNo)
        var query: String = ""
        try {
            query = if (receiver == ReceiverType.COMPANY)
                "SELECT email FROM owners " +
                        "JOIN companies ON owners.id = companies.owner_id " +
                        "JOIN company_invoices ON company_invoices.company_id = companies.id WHERE company_invoices.invoice_no= ?"
            else
                "SELECT email FROM customers JOIN personal_invoices ON personal_invoices.customer_id= customers.id WHERE invoice_no= ?"
            resultSet = DatabaseQueryUtils
                    .createQuery(query, params)
            if (resultSet.next())
                return resultSet.getString("email")
            return null

        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun getCompanyInvoiceSummary(): ArrayList<InvoiceSummary> {
        val list: ArrayList<InvoiceSummary> = arrayListOf()
        resultSet = DatabaseQueryUtils
                .createQuery("SELECT invoice_no, date_of_issue, amount, companies.company_name AS receiver, invoices_links.pdf_link " +
                        "FROM company_invoices " +
                        "JOIN companies ON company_invoices.company_id = companies.id " +
                        "LEFT OUTER JOIN invoices_links ON invoices_links.invoice_company_no = company_invoices.invoice_no " +
                        "ORDER BY date_of_issue DESC", emptyMap())
        if (resultSet.next()) {
            do {
                list.add(getSummaryFromResultSet(resultSet))
            } while (resultSet.next())
        }
        return list
    }

    fun getPersonalInvoiceSummary(): ArrayList<InvoiceSummary> {
        val list: ArrayList<InvoiceSummary> = arrayListOf()
        resultSet = DatabaseQueryUtils
                .createQuery("SELECT invoice_no, date_of_issue, amount, CONCAT( customers.name, ' ', customers.surname) AS receiver, invoices_links.pdf_link " +
                        "FROM personal_invoices " +
                        "JOIN customers ON personal_invoices.customer_id=customers.id " +
                        "LEFT OUTER JOIN invoices_links ON invoices_links.invoice_personal_no = personal_invoices.invoice_no " +
                        "ORDER BY date_of_issue DESC", emptyMap())
        if (resultSet.next())
            do {
                list.add(getSummaryFromResultSet(resultSet))
            } while (resultSet.next())
        return list
    }

    fun insertPDFlink(link: String, invoiceNo: String, receiver: ReceiverType): Boolean {
        var rowsAff = 0
        val params = linkedMapOf(1 to link, 2 to null)
        try {
            if (receiver == ReceiverType.PERSON) {
                params[2] = null
                params[3] = invoiceNo
            } else if (receiver == ReceiverType.COMPANY) {
                params[2] = invoiceNo
                params[3] = null
            }
            rowsAff = DatabaseQueryUtils
                    .createUpdateTypeQuery("INSERT INTO invoices_links(pdf_link, invoice_company_no, invoice_personal_no) VALUES(?,?,?)", params)
            if (rowsAff > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertCompanyInvoice(companyInvoice: CompanyInvoice, items: ObservableList<Item>): String {

        val invoiceNo = generateInvoiceNo()
        val params = linkedMapOf(1 to invoiceNo, 2 to convertToDate(companyInvoice.dateOfIssue), 3 to companyInvoice.amount,
                4 to companyInvoice.paymentMethod.toString(), 5 to companyInvoice.creator.id)
        try {
            val isCompanyExisting = findCompanyByName(companyInvoice.company.companyName)
            if (isCompanyExisting != null)
                params[6] = isCompanyExisting.id
            else {
                insertCompany(companyInvoice.company)
                val companyToInsert = findCompanyByName(companyInvoice.company.companyName)
                params[6] = companyToInsert!!.id
            }
            DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO company_invoices(invoice_no, date_of_issue, amount, payment_option, prepared_by, company_id) VALUES(?,?,?,?,?,?)", params)

            for (item in items) {
                insertItem(item, invoiceNo, ReceiverType.COMPANY)
            }
            saveLinkToCompanyInvoices(companyInvoice, invoiceNo, items)
            return invoiceNo
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    private fun saveLinkToCompanyInvoices(companyInvoice: CompanyInvoice, invoiceNo: String, items: ObservableList<Item>) {
        companyInvoice.invoiceNo = invoiceNo
        val itemsList = items.stream().collect(Collectors.toList())
        val path = PDFGenerator.generatePDFCompanyInvoice(companyInvoice, itemsList as java.util.ArrayList<Item>?)
        val s3path = s3.upload(invoiceNo, path)
        insertPDFlink(s3path, invoiceNo, receiver = ReceiverType.COMPANY)
    }

    fun insertPersonalInvoice(personalInvoice: PersonalInvoice, items: ObservableList<Item>): String {
        val invoiceNo = generateInvoiceNo()
        val params = linkedMapOf(1 to invoiceNo, 2 to convertToDate(personalInvoice.dateOfIssue), 3 to personalInvoice.amount, 4 to personalInvoice.paymentMethod.toString(), 5 to personalInvoice.discount)
        try {
            val isCustomerExisting = findCustomerByEmail(personalInvoice.customer.email)
            if (isCustomerExisting != null) {
                params[6] = isCustomerExisting.id
            } else {
                insertCustomer(personalInvoice.customer)
                val insertedCustomer = findCustomerByEmail(personalInvoice.customer.email)
                params[6] = insertedCustomer!!.id
            }
            params[7] = getLoggedUser().id
            DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO personal_invoices(invoice_no, date_of_issue, amount, payment_option, discount, customer_id, prepared_by) VALUES(?,?,?,?,?,?,?)", params)
            for (item in items) {
                insertItem(item, invoiceNo, ReceiverType.PERSON)
            }
            saveLinkToPersonalInvoices(personalInvoice, invoiceNo, items)
            return invoiceNo
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    private fun saveLinkToPersonalInvoices(personalInvoice: PersonalInvoice, invoiceNo: String, items: ObservableList<Item>) {
        personalInvoice.invoiceNo = invoiceNo
        val itemsList = items.stream().collect(Collectors.toList())
        val path = PDFGenerator.generatePDFPersonalInvoice(personalInvoice, itemsList as java.util.ArrayList<Item>?)
        val s3path = s3.upload(invoiceNo, path)
        insertPDFlink(s3path, invoiceNo, receiver = ReceiverType.PERSON)
    }

    fun insertCompany(company: Company): Boolean {
        var rowsAffected = 0
        lateinit var params: Map<Int, Any>

        val isCompanyExisting = findCompanyByName(company.companyName)
        if (isCompanyExisting != null) return true

        try {
            val isExistingOwner = findOwnerByPid(company.owner.pid.toLong())
            val isExistingAddress = findAddressDetails(company.address)
            if (isExistingOwner != null) {
                if (isExistingAddress != null) {
                    params = mapOf(1 to company.companyName, 2 to company.nip, 3 to isExistingAddress.id, 4 to isExistingOwner.id)
                }
                insertAddressDetails(company.address)
                val addressDetails = findAddressDetails(company.address)
                params = mapOf(1 to company.companyName, 2 to company.nip, 3 to addressDetails!!.id, 4 to isExistingOwner.id)
            } else {
                insertOwner(company.owner)
                val owner = findOwnerByPid(company.owner.pid.toLong())
                insertAddressDetails(company.address)
                val address = findAddressDetails(company.address)
                params = mapOf(1 to company.companyName, 2 to company.nip, 3 to address!!.id, 4 to owner!!.id)
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO companies(company_name, nip, address_id, owner_id) VALUES(?,?,?,?)", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findCompanyByName(name: String): Company? {
        try {
            val params = mapOf(1 to name)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, company_name, nip, address_id, owner_id FROM companies WHERE company_name= ?", params)
            if (resultSet.next()) {
                return getCompanyFromResultSet(resultSet)
            }
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findCompanyById(id: Long): Company? {
        try {
            val params = mapOf(1 to id)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, company_name, nip, address_id, owner_id FROM companies WHERE id= ?", params)
            if (resultSet.next()) {
                return getCompanyFromResultSet(resultSet)
            }
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun deleteCompanyByName(name: String): Boolean {
        var rowsAffected = 0
        try {
            val params = mapOf(1 to name)
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("DELETE FROM companies WHERE company_name= ?", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertOwner(owner: Owner): Int {
        try {
            val isExistingOwner = findOwnerByPid(owner.pid.toLong())
            if (isExistingOwner != null) return isExistingOwner.id.toInt()
            val params = mapOf(1 to owner.name, 2 to owner.surname, 3 to owner.email, 4 to owner.phoneNumber, 5 to owner.pid)
            return DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO owners(name, surname,email, phone_number, pid) VALUES(?,?,?,?,?)", params)
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertCustomer(customer: Customer): Boolean {
        val params = linkedMapOf(1 to customer.name, 2 to customer.surname, 3 to customer.email, 4 to customer.phoneNumber)
        var rowsAffected = 0
        try {
            val isExistingCustomer = findCustomerByEmail(customer.email)
            if (isExistingCustomer != null) return true
            val isExistingAddressDetails = findAddressDetails(customer.address)
            if (isExistingAddressDetails != null) {
                params[5] = isExistingAddressDetails.id
            } else {
                insertAddressDetails(customer.address)
                val insertedAddressDetails = findAddressDetails(customer.address)
                params[5] = insertedAddressDetails!!.id
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO customers(name, surname, email, phone_number, address_id) VALUES(?,?,?,?,?)", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findCustomerByEmail(email: String): Customer? {
        try {
            val params = mapOf(1 to email)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, name, surname, email, phone_number, address_id FROM customers WHERE email= ?", params)
            if (resultSet.next()) return getCustomerFromResultSet(resultSet)
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findCustomerById(id: Long): Customer? {
        try {
            val params = mapOf(1 to id)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, name, surname, email, phone_number, address_id FROM customers WHERE id= ?", params)
            if (resultSet.next()) return getCustomerFromResultSet(resultSet)
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertAddressDetails(address: AddressDetails): Int {
        try {
            val isExistingAddress = findAddressDetails(address)
            if (isExistingAddress != null) return isExistingAddress.id.toInt()
            val params = mapOf(1 to address.country, 2 to address.city, 3 to address.street, 4 to address.building)
            return DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO address_details(country, city, street, building) VALUES(?,?,?,?)", params)
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun deleteAddressDetailsById(addrId: Int): Boolean {
        var rowsAffected = 0
        try {
            val params = mapOf(1 to addrId)
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("DELETE FROM address_details WHERE id= ? ", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findAddressDetailsById(addrId: Int): AddressDetails? {
        try {
            val params = mapOf(1 to addrId)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, country, city, street, building FROM address_details WHERE id= ?", params)
            if (resultSet.next()) return getAddressFromResultSet(resultSet)
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findAddressDetails(address: AddressDetails): AddressDetails? {
        try {
            val params = mapOf(1 to address.country, 2 to address.city, 3 to address.street, 4 to address.building)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, country, city, street, building FROM address_details WHERE country= ? AND city= ? AND street= ? AND building= ?", params)
            if (resultSet.next()) return getAddressFromResultSet(resultSet)
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findOwnerByPid(pid: Long): Owner? {
        try {
            val params = mapOf(1 to pid)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, name, surname, email, phone_number,pid FROM owners WHERE pid= ?", params)
            if (resultSet.next()) return getOwnerFromResultSet(resultSet)
            return null;

        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findOwnerById(ownerId: Long): Owner? {
        try {
            val params = mapOf(1 to ownerId)
            resultSet = DatabaseQueryUtils.createQuery("SELECT id, name, surname, email, phone_number,pid FROM owners WHERE id= ?", params)
            if (resultSet.next()) return getOwnerFromResultSet(resultSet)
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun deleteOwnerByPid(pid: Long): Boolean {
        var rowsAffected = 0
        try {
            val params = mapOf(1 to pid)
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("DELETE FROM owners WHERE pid= ?", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertItem(item: Item, invoiceNo: String, receiver: ReceiverType): Boolean {
        var rowsAffected = 0
        val params = linkedMapOf(1 to item.description, 2 to item.cost, 3 to item.quantity, 4 to item.vat, 5 to item.category.toString())
        try {
            if (receiver == ReceiverType.COMPANY) {
                params[6] = invoiceNo
                params[7] = null
            } else if (receiver == ReceiverType.PERSON) {
                params[6] = null
                params[7] = invoiceNo
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO items(description, cost, quantity, vat, category, invoice_company_no, invoice_personal_no) VALUES(?,?,?,?,?,?,?)", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")

        }
    }

    fun findItemsByInvoiceNo(invoiceNo: String, receiver: ReceiverType): ArrayList<Item> {
        val items = ArrayList<Item>()
        val params = mapOf(1 to invoiceNo)
        resultSet = if (receiver == ReceiverType.COMPANY) {
            DatabaseQueryUtils
                    .createQuery("SELECT id, description, cost, quantity, vat, category, invoice_company_no AS invoice_no FROM items WHERE invoice_company_no= ?", params)
        } else DatabaseQueryUtils
                .createQuery("SELECT id, description, cost, quantity, vat, category, invoice_personal_no AS invoice_no FROM items WHERE invoice_personal_no= ?", params)
        if (resultSet.next()) {
            do {
                items.add(getItemFroResultSet(resultSet))
            } while (resultSet.next())
        }
        return items
    }

    fun findCompanyInvoiceByInvoiceNo(invoiceNo: String): CompanyInvoice? {
        val params = mapOf(1 to invoiceNo)
        try {
            resultSet = DatabaseQueryUtils
                    .createQuery("SELECT invoice_no, date_of_issue, amount, payment_option, prepared_by, company_id FROM company_invoices WHERE invoice_no= ?", params)
            if (resultSet.next()) {
                val companyInvoice = getCompanyInvoiceFromResultSet(resultSet)
                companyInvoice.items = findItemsByInvoiceNo(companyInvoice.invoiceNo, ReceiverType.COMPANY)
                return companyInvoice
            }
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findPersonalInvoiceByInvoiceNo(invoiceNo: String): PersonalInvoice? {
        val params = mapOf(1 to invoiceNo)
        try {
            resultSet = DatabaseQueryUtils
                    .createQuery("SELECT invoice_no, date_of_issue, amount, payment_option, discount, customer_id, prepared_by FROM personal_invoices WHERE invoice_no= ?", params)
            if (resultSet.next()) {
                val personalInvoice = getPersonalInvoiceFromResultSet(resultSet)
                personalInvoice.items = findItemsByInvoiceNo(personalInvoice.invoiceNo, ReceiverType.PERSON)
                return personalInvoice
            }
            return null
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }
}




