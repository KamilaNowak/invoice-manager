package com.nowak.demo.database

import com.nowak.demo.models.addresses.AddressDetails
import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.Customer
import com.nowak.demo.models.customers.Owner
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.invoices.PersonalInvoice
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.items.ReceiverType
import com.nowak.demo.models.summary.InvoiceSummary
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class InvoicesDatabase {

    val resultSetProperty = SimpleObjectProperty<ResultSet>()
    var resultSet by resultSetProperty


    fun getCompanyInvoiceSummary(): ArrayList<InvoiceSummary> {
        val list: ArrayList<InvoiceSummary> = arrayListOf()
        resultSet = DatabaseQueryUtils
                .createQuery("SELECT invoice_no, date_of_issue, amount, companies.company_name AS receiver, invoices_links.pdf_link " +
                        "FROM company_invoices " +
                        "JOIN companies ON company_invoices.company_id = companies.id " +
                        "LEFT OUTER JOIN invoices_links ON invoices_links.invoice_company_no = company_invoices.invoice_no " +
                        "ORDER BY date_of_issue DESC", emptyMap())
        if (resultSet.next()) {
            do { list.add(getSummaryFromResultSet(resultSet)) } while (resultSet.next()) }
        return list
    }
    fun getPersonalInvoiceSummary(): ArrayList<InvoiceSummary>{
        val list: ArrayList<InvoiceSummary> = arrayListOf()
        resultSet= DatabaseQueryUtils
                .createQuery("SELECT invoice_no, date_of_issue, amount, CONCAT( customers.name, ' ', customers.surname) AS receiver, invoices_links.pdf_link " +
                        "FROM personal_invoices " +
                        "JOIN customers ON personal_invoices.customer_id=customers.id " +
                        "LEFT OUTER JOIN invoices_links ON invoices_links.invoice_personal_no = personal_invoices.invoice_no " +
                        "ORDER BY date_of_issue DESC", emptyMap())
        if (resultSet.next())
            do { list.add(getSummaryFromResultSet(resultSet)) } while (resultSet.next())
        return list
    }

    fun insertPDFlink(link: String, invoiceNo: String, receiver: ReceiverType): Boolean{
        var rowsAff =0
        val params = linkedMapOf(1 to link,2 to null)
        try {
            if (receiver == ReceiverType.PERSON) {
                params[2] = null
                params[3] = invoiceNo
            } else if (receiver == ReceiverType.COMPANY) {
                params[2] = invoiceNo
                params[3] = null
            }
            rowsAff = DatabaseQueryUtils
                    .createUpdateTypeQuery("INSERT INTO invoices_links(pdf_link, invoice_company_no, invoice_personal_no) VALUES(?,?,?)",params)
            if(rowsAff>0)return true
            return false
        }catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertCompanyInvoice(companyInvoice: CompanyInvoice, item: Item): Boolean {

        val invoiceNo = generateInvoiceNo()
        val params = linkedMapOf(1 to invoiceNo, 2 to convertToDate(companyInvoice.dateOfIssue), 3 to companyInvoice.quantity, 4 to companyInvoice.amount,
                5 to companyInvoice.paymentMethod.toString(), 6 to companyInvoice.creator.id)
        var rowsAffected = 0
        try {
            val isCompanyExisting = findCompanyByName(companyInvoice.company.companyName)
            if (isCompanyExisting != null)
                params[7] = isCompanyExisting.id
            else {
                insertCompany(companyInvoice.company)
                val companyToInsert = findCompanyByName(companyInvoice.company.companyName)
                params[7] = companyToInsert!!.id
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO company_invoices(invoice_no, date_of_issue, quantity, amount, payment_option, prepared_by, company_id) VALUES(?,?,?,?,?,?,?)", params)
            insertItem(item, invoiceNo, ReceiverType.COMPANY)
            insertPDFlink("Goodle.com",invoiceNo, ReceiverType.COMPANY)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertPersonalInvoice(personalInvoice: PersonalInvoice, item: Item): Boolean {
        val invoiceNo = generateInvoiceNo()
        val params = linkedMapOf(1 to invoiceNo, 2 to convertToDate(personalInvoice.dateOfIssue), 3 to personalInvoice.quantity, 4 to personalInvoice.amount, 5 to personalInvoice.paymentMethod.toString(), 6 to personalInvoice.discount)
        var rowsAffected = 0
        try {
            val isCustomerExisting = findCustomerByEmail(personalInvoice.customer.email)
            if (isCustomerExisting != null) {
                params[7] = isCustomerExisting.id
            } else {
                insertCustomer(personalInvoice.customer)
                val insertedCustomer = findCustomerByEmail(personalInvoice.customer.email)
                params[7] = insertedCustomer!!.id
            }
            params[8] = getLoggedUser().id
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO personal_invoices(invoice_no, date_of_issue, quantity, amount, payment_option, discount, customer_id, prepared_by) VALUES(?,?,?,?,?,?,?,?)", params)
            insertItem(item, invoiceNo, ReceiverType.PERSON)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
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
        val params = linkedMapOf(1 to item.description, 2 to item.vat, 3 to item.category.toString())
        try {
            if (receiver == ReceiverType.COMPANY) {
                params[4] = invoiceNo
                params[5] = null
            } else if (receiver == ReceiverType.PERSON) {
                params[4] = null
                params[5] = invoiceNo
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO items(description, vat, category, invoice_company_no, invoice_personal_no) VALUES(?,?,?,?,?)", params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    private fun getOwnerFromResultSet(resultSet: ResultSet): Owner {
        return Owner(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getLong("phone_number"),
                resultSet.getInt("pid"))
    }

    private fun getAddressFromResultSet(resultSet: ResultSet): AddressDetails {
        return AddressDetails(resultSet.getLong("id"),
                resultSet.getString("country"),
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getInt("building"))
    }

    private fun getCompanyFromResultSet(resultSet: ResultSet): Company {
        return Company(resultSet.getLong("id"),
                resultSet.getString("company_name"),
                resultSet.getLong("nip"),
                findAddressDetailsById(resultSet.getInt("address_id"))!!,
                findOwnerById(resultSet.getInt("owner_id").toLong())!!)
    }

    private fun getCustomerFromResultSet(resultSet: ResultSet): Customer {
        return Customer(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getLong("phone_number"),
                findAddressDetailsById(resultSet.getInt("address_id"))!!)
    }

    fun generateInvoiceNo(): String {
        val prefix = " INV"
        val date = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return prefix + date.toString()
    }

    private fun getSummaryFromResultSet(resultSet: ResultSet): InvoiceSummary {
        return InvoiceSummary(resultSet.getString("invoice_no"),
                resultSet.getDate("date_of_issue").toLocalDate(),
                resultSet.getInt("amount"),
                resultSet.getString("receiver"),
                resultSet.getString("pdf_link"))
    }
}


