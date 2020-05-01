package com.nowak.demo.database

import java.sql.*
import java.util.logging.Logger

class DatabaseQueryUtils {

    companion object Query {

        private var connection: Connection

        init {
            connection = DatabaseUtils.getConnection()
        }

        fun createQuery(query: String, parameters: Map<Int, Any>): ResultSet {
            val preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
            try {
                setParameters(preparedStatement, parameters)
                val res = preparedStatement.executeQuery()
                connection.commit()
                return res
            } catch (e: Exception) {
                connection.rollback()
                throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
            }
        }

        fun createUpdateTypeQuery(query: String, parameters: Map<Int, Any?> = emptyMap()): Int {
            val preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, Statement.RETURN_GENERATED_KEYS)
            try {
                if (parameters.isNotEmpty()) {
                    setParameters(preparedStatement, parameters as Map<Int, Any>)
                }
                val res= preparedStatement.executeUpdate()
                connection.commit()
                return res
            } catch (e: Exception) {
                connection.rollback()
                throw SQLException("Cannot execute query: Cause: ${e.cause} ; Message: ${e.message}")
            }
        }

        private fun setParameters(preparesStatement: PreparedStatement, parameters: Map<Int, Any>): PreparedStatement {
            parameters.forEach { (key, value) -> preparesStatement.setObject(key, value) }
            return preparesStatement
        }
    }
}