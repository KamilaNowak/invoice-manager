package com.nowak.demo.database

import com.nowak.demo.models.login.User
import java.sql.*
import java.time.*
import kotlin.collections.ArrayList

class InvoiceDatabase() {

    private var connection: Connection

    init {
        connection = DatabaseUtils.getConnection()
    }

    fun createQuery(query: String, parameters: Map<Int, Any>): ResultSet {
        val preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
        try {
            setParameters(preparedStatement, parameters)
            return preparedStatement.executeQuery()
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    private fun createUpdateTypeQuery(query: String, parameters: Map<Int, Any?> = emptyMap()): Int {
        val preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
        try {
            if (parameters.isNotEmpty()) {
                setParameters(preparedStatement, parameters as Map<Int, Any>)
            }
            return preparedStatement.executeUpdate()
        } catch (e: Exception) {
            throw SQLException("Cannot execute query: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    private fun setParameters(preparesStatement: PreparedStatement, parameters: Map<Int, Any>): PreparedStatement {
        parameters.forEach { (key, value) -> preparesStatement.setObject(key, value) }
        return preparesStatement
    }
    fun getUserFromResultSet(resultSet:ResultSet): User{
        return User(
                resultSet.getInt("id").toLong(),
                resultSet.getString("username"),
                resultSet.getString("passwd"),
                resultSet.getString("email"),
                resultSet.getDate("date_of_birth").toLocalDate(),
                resultSet.getDate("createdAt").toLocalDate())
    }

    fun findAllUsers(): ArrayList<User> {
        try {
            val userList = ArrayList<User>()
            val resultSet = createQuery("SELECT id,username,passwd, email,date_of_birth, createdAt FROM users", emptyMap())
            if (!resultSet.next()) return emptyList<User>() as ArrayList<User>
            do{
                userList.add(getUserFromResultSet(resultSet))
            }while(resultSet.next())
            return userList
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findUserByUsername(username: String): User {
        lateinit var resultSet: ResultSet
        try {
            val params = mapOf(1 to username)
            resultSet = createQuery("SELECT id, username, passwd, email, date_of_birth, createdAt FROM users WHERE username = ?", params)
            resultSet.next()
            return getUserFromResultSet(resultSet)
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findUserById(userId: Long): User {
        lateinit var resultSet: ResultSet
        try {
            val params = mapOf(1 to userId)
            val resultNames = mapOf(1 to "id", 2 to "username", 3 to "passwd", 4 to "email", 5 to "date_of_birth", 6 to "createdAt")
            resultSet = createQuery("SELECT id, username, passwd, email, date_of_birth, createdAt FROM users WHERE id = ?", params)
            resultSet.next()
            return getUserFromResultSet(resultSet)
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun checkIfAccountExists(username: String, password: String): Boolean {
        lateinit var resultSet: ResultSet
        try {
            val params = mapOf(1 to username)
            resultSet = createQuery("SELECT id,passwd FROM users WHERE username= ?", params)
            resultSet.next()
            val id = resultSet.getInt("id")
            if (id > 0) {
                val hashedPassword = resultSet.getString("passwd")
                return verifyPassword(password, hashedPassword)
            }
            return false
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun checkUsernameEmailAvailability(username: String? = null, email: String? = null): Boolean {
        lateinit var resultSet: ResultSet
        try {
            if (username != null && email == null) {
                val params = mapOf(1 to username)
                resultSet = createQuery("SELECT id FROM users WHERE username= ?", params)
            }
            if (username == null && email != null) {
                val params = mapOf(1 to email)
                resultSet = createQuery("SELECT id FROM users WHERE email= ?", params)
            } else if (username != null && email != null) {
                val params = mapOf(1 to username, 2 to email)
                resultSet = createQuery("SELECT id FROM users WHERE username= ? OR email= ?", params)
            }
            if (!resultSet.next()) return false
            val id = resultSet.getInt("id")
            return (id > 0)
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun insertNewUser(username: String, password: String, email: String, birthDate: LocalDate): Int {
        try {
            val params = mapOf(1 to username, 2 to hashPassword(password), 3 to email,
                    4 to convertToDate(birthDate), 5 to convertToDate(LocalDate.now()))
            return createUpdateTypeQuery("INSERT INTO users(username, passwd, email, date_of_birth, createdAt) VALUES(?,?,?,?,?)", params)
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun updateUser(userId: Long, newUsername: String? = null, newEmail: String? = null, newBirthDate: LocalDate? = null, newPassword: String? = null): Boolean {
        try {
            var rowsAffected = 0
            if (newUsername != null) {
                val params = mapOf(1 to newUsername, 2 to userId)
                rowsAffected = createUpdateTypeQuery("UPDATE users SET username= ? WHERE id= ?", params)
            }
            if (newEmail != null) {
                val params = mapOf(1 to newEmail, 2 to userId)
                rowsAffected = createUpdateTypeQuery("UPDATE users SET email= ? WHERE id= ?", params)
            }
            if (newBirthDate != null) {
                val params = mapOf(1 to convertToDate(newBirthDate), 2 to userId)
                rowsAffected = createUpdateTypeQuery("UPDATE users SET date_of_birth= ? WHERE id= ?", params)
            }
            if (newPassword != null) {
                val params = mapOf(1 to hashPassword(newPassword), 2 to userId)
                rowsAffected = createUpdateTypeQuery("UPDATE users SET passwd= ? WHERE id= ?", params)
            }
            if (rowsAffected > 0) return true
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
        return false
    }

    fun getPasswordByUserId(userId: Long): String {
        lateinit var resultSet: ResultSet
        try {
            val params = mapOf(1 to userId)
            resultSet = createQuery("SELECT passwd FROM users WHERE id= ?", params)
            resultSet.next()
            return resultSet.getString("passwd")
        } catch (e: Exception) {
            throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
        }
    }
    fun deleteUserByUsername(username: String) : Boolean{
        var rowsAffected =0;
        try{
            val params = mapOf(1 to username)
            rowsAffected = createUpdateTypeQuery("DELETE FROM users WHERE username= ?", params)
            if(rowsAffected>0) return true
            return false
        }   catch (e: Exception) {
        throw SQLException("Cannot extract data: Cause: ${e.cause} ; Message: ${e.message}")
    }
    }

}