package com.nowak.demo.database

import com.nowak.demo.models.login.User
import tornadofx.*
import java.lang.Exception
import java.sql.*
import java.time.LocalDate
import javax.sql.DataSource

class InvoicerDatabase() {

    //development
    val jdbcUrl = "jdbc:postgresql://localhost:5432/invoicer"
    val username = "postgres"
    val password = "root"

    lateinit var connection: Connection

    fun connect(): Connection {
        Class.forName("org.postgresql.Driver")
        return DriverManager.getConnection(jdbcUrl, username, password)
    }

    init {
        connection = connect()
    }

    fun findAll(): User {
        try {
            val preparedStatement = connection
                    .prepareStatement(
                            "SELECT id,username,passwd, email,date_of_birth FROM users")
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val user = User(
                    resultSet.getInt("id").toLong(),
                    resultSet.getString("username"),
                    resultSet.getString("passwd"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth").toLocalDate())
            preparedStatement.close()
            return user
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findUserByUsername(username: String): User {
        try {
            val preparedStatement = connection
                    .prepareStatement(
                            "SELECT id, username, passwd, email,date_of_birth FROM users WHERE username = ?")
            preparedStatement.setString(1, username)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val user = User(
                    resultSet.getInt("id").toLong(),
                    resultSet.getString("username"),
                    resultSet.getString("passwd"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth").toLocalDate())
            preparedStatement.close()
            return user
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun checkIfAccountExists(username: String, password: String): Boolean {
        try {
            val preparedStatement = connection
                    .prepareStatement("SELECT id FROM users WHERE username= ? AND passwd= ?")
            preparedStatement.setString(1, username)
            preparedStatement.setString(2, password)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val id = resultSet.getInt("id")
            preparedStatement.close()
            return (id > 0 && id != null)
        } catch (e: Exception) {
            return false
        }
    }

    fun checkUsernameEmailAvailability(username: String, email: String): Boolean {
        try {
            val preparedStatement = connection
                    .prepareStatement("Select id FROM users WHERE username= ? OR email= ?")
            preparedStatement.setString(1, username)
            preparedStatement.setString(2, email)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val id = resultSet.getInt("id")
            preparedStatement.close()
            return id > 0
        } catch (e: Exception) {
            return false
        }
    }

    fun insertNewUser(username: String, password: String, email: String, birthDate: LocalDate): Int {
        try {
            val preparedStatement = connection
                    .prepareStatement("INSERT INTO users(username, passwd, email, date_of_birth) VALUES(?,?,?,?)")
            preparedStatement.setString(1, username)
            preparedStatement.setString(2, password)
            preparedStatement.setString(3, email)
            preparedStatement.setDate(4, convertToDate(birthDate))
            val row = preparedStatement.executeUpdate()
            preparedStatement.close()
            return row
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
            return -1
        }
    }
}