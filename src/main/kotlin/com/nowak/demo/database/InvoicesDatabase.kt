package com.nowak.demo.database

import com.nowak.demo.models.addresses.AddressDetails
import com.nowak.demo.models.customers.Company
import com.nowak.demo.models.customers.Owner
import com.nowak.demo.models.invoices.CompanyInvoice
import com.nowak.demo.models.items.Item
import com.nowak.demo.models.items.ReceiverType
import java.sql.ResultSet
import java.sql.SQLException
import javax.sound.midi.Receiver

class InvoicesDatabase {

    private lateinit var resultSet: ResultSet

    fun insertCompanyInvoice(companyInvoice: CompanyInvoice): Boolean {
        val params = linkedMapOf(1 to companyInvoice.invoiceNo, 2 to convertToDate(companyInvoice.dateOfIssue), 3 to companyInvoice.quantity, 4 to companyInvoice.amount,
                5 to companyInvoice.paymentMethod.toString(), 6 to companyInvoice.creator.id)
        var rowsAffected = 0
        try {
            val isCompanyExisting = findCompanyByName(companyInvoice.company.companyName)
            if (isCompanyExisting != null)
                params[7] = isCompanyExisting.id
            else {
                insertCompany(companyInvoice.company)
                val companyToInsert = findCompanyByName(companyInvoice.company.companyName)
                println("Company id: " + companyToInsert.toString())
                params[7] = companyToInsert!!.id
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO company_invoices(invoice_no, date_of_issue, quantity, amount, payment_option, prepared_by, company_id) VALUES(?,?,?,?,?,?,?)", params)
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
            if(receiver == ReceiverType.COMPANY) {
                params[4] = invoiceNo
                params[5] = null
            }
            else if(receiver==ReceiverType.PERSON){
                params[4] =null
                params[5]=invoiceNo
            }
            rowsAffected = DatabaseQueryUtils.createUpdateTypeQuery("INSERT INTO items(description, vat,category, invoice_comp_no, invoice_pers_no) VALUES(?,?,?,?,?)",params)
            if (rowsAffected > 0) return true
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }


    private fun getOwnerFromResultSet(resultSet: ResultSet): Owner {
        return Owner(resultSet.getInt("id").toLong(),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getLong("phone_number"),
                resultSet.getInt("pid"))
    }

    private fun getAddressFromResultSet(resultSet: ResultSet): AddressDetails {
        return AddressDetails(resultSet.getInt("id").toLong(),
                resultSet.getString("country"),
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getInt("building"))
    }

    private fun getCompanyFromResultSet(resultSet: ResultSet): Company {
        return Company(resultSet.getInt("id").toLong(),
                resultSet.getString("company_name"),
                resultSet.getLong("nip"),
                findAddressDetailsById(resultSet.getInt("address_id"))!!,
                findOwnerById(resultSet.getInt("owner_id").toLong())!!)
    }

}