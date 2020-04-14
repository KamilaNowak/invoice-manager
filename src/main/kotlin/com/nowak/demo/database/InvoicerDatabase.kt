package com.nowak.demo.database

import com.nowak.demo.models.login.User
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.time.LocalDate
import java.util.*

class InvoicerDatabase() {

    //    val properties = Properties()
//    val inputStream = FileInputStream("src\\main\\resources\\app.properties")
//
//    var jdbcUrl: String = ""
//    var username: String = ""
//    var password: String = ""
    val jdbcUrl: String = "jdbc:postgresql://localhost:5432/invoicer"
    val username: String = "postgres"
    val password: String = "root"
    lateinit var connection: Connection

    init {
        connection = connect()
//        properties.load(inputStream)
//        jdbcUrl = properties.getProperty("db.url")
//        username = properties.getProperty("db.username")
//        password = properties.getProperty("db.password")
    }

    fun connect(): Connection {
        Class.forName("org.postgresql.Driver")
        return DriverManager.getConnection(jdbcUrl, username, password)
    }

    fun findAll(): User {
        try {
            val preparedStatement = connection
                    .prepareStatement(
                            "SELECT id,username,passwd, email,date_of_birth, createdAt FROM users")
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val user = User(
                    resultSet.getInt("id").toLong(),
                    resultSet.getString("username"),
                    resultSet.getString("passwd"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth").toLocalDate(),
                    resultSet.getDate("createdAt").toLocalDate())
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
                            "SELECT id, username, passwd, email, date_of_birth, createdAt FROM users WHERE username = ?")
            preparedStatement.setString(1, username)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val user = User(
                    resultSet.getInt("id").toLong(),
                    resultSet.getString("username"),
                    resultSet.getString("passwd"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth").toLocalDate(),
                    resultSet.getDate("createdAt").toLocalDate())
            preparedStatement.close()
            return user
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun findUserById(userId: Long): User {
        try {
            val preparedStatement = connection
                    .prepareStatement(
                            "SELECT id, username, passwd, email, date_of_birth, createdAt FROM users WHERE id = ?")
            preparedStatement.setLong(1, userId)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val user = User(
                    resultSet.getInt("id").toLong(),
                    resultSet.getString("username"),
                    resultSet.getString("passwd"),
                    resultSet.getString("email"),
                    resultSet.getDate("date_of_birth").toLocalDate(),
                    resultSet.getDate("createdAt").toLocalDate())
            preparedStatement.close()
            return user
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun checkIfAccountExists(username: String, password: String): Boolean {
        try {
            val preparedStatement = connection
                    .prepareStatement("SELECT id,passwd FROM users WHERE username= ?")
            preparedStatement.setString(1, username)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val id = resultSet.getInt("id")
            val hashedPassword = resultSet.getString("passwd")
            preparedStatement.close()
            var pwdMatches: Boolean = false
            if (id > 0) pwdMatches = verifyPassword(password, hashedPassword)
            return pwdMatches
        } catch (e: Exception) {
            return false
        }
    }

    fun checkUsernameEmailAvailability(username: String? = null, email: String? = null): Boolean {
        try {
            var preparedStatement = connection.prepareStatement("")
            if (username != null && email == null) {
                preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE username= ?")
                preparedStatement.setString(1, username)
            }
            if (username == null && email != null) {
                preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE email= ?")
                preparedStatement.setString(1, email)
            } else if (username != null && email != null) {
                preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE username= ? AND email= ?")
                preparedStatement.setString(1, username)
                preparedStatement.setString(2, email)
            }
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            val id = resultSet.getInt("id")
            preparedStatement.close()
            return (id > 0)
        } catch (e: Exception) {
            return false
        }
    }


    fun insertNewUser(username: String, password: String, email: String, birthDate: LocalDate): Int {
        try {
            val preparedStatement = connection
                    .prepareStatement("INSERT INTO users(username, passwd, email, date_of_birth, createdAt) VALUES(?,?,?,?,?)")
            preparedStatement.setString(1, username)
            preparedStatement.setString(2, hashPassword(password))
            preparedStatement.setString(3, email)
            preparedStatement.setDate(4, convertToDate(birthDate))
            preparedStatement.setDate(5, convertToDate(LocalDate.now()))
            val row = preparedStatement.executeUpdate()
            preparedStatement.close()
            return row
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }

    fun updateUser(userId: Long, newUsername: String? = null, newEmail: String? = null, newBirthDate: LocalDate? = null, newPassword: String? = null): Boolean {
        try {
            var preparedStatement: PreparedStatement = connection.prepareStatement("")

            if (newUsername != null) {
                preparedStatement = connection.prepareStatement("UPDATE users SET username= ? WHERE id= ?")
                preparedStatement.setString(1, newUsername)
                preparedStatement.setLong(2, userId)
            }
            if (newEmail != null) {
                preparedStatement = connection.prepareStatement("UPDATE users SET email= ? WHERE id= ?")
                preparedStatement.setString(1, newEmail)
                preparedStatement.setLong(2, userId)
            }
            if (newBirthDate != null) {
                preparedStatement = connection.prepareStatement("UPDATE users SET date_of_birth= ? WHERE id= ?")
                preparedStatement.setDate(1, convertToDate(newBirthDate))
                preparedStatement.setLong(2, userId)
            }
            if (newPassword != null) {
                preparedStatement = connection.prepareStatement("UPDATE users SET passwd= ? WHERE id= ?")
                preparedStatement.setString(1, hashPassword(newPassword))
                preparedStatement.setLong(2, userId)
            }
            preparedStatement.executeUpdate()
            preparedStatement.close()
            return true
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
        return false
    }

    fun getPasswordByUserId(userId: Long): String {
        try {
            val preparedStatement = connection.prepareStatement("SELECT passwd FROM users WHERE id= ?")
            preparedStatement.setLong(1, userId)
            val resultSet = preparedStatement.executeQuery()
            resultSet.next()
            return resultSet.getString("passwd")
        } catch (e: Exception) {
            throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
        }
    }
}