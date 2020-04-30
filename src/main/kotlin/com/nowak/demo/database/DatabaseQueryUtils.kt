package com.nowak.demo.database

import java.sql.*

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
                return preparedStatement.executeQuery()
            } catch (e: Exception) {
                throw SQLException("Cannot execute query : Cause: ${e.cause} ; Message: ${e.message}")
            }
        }

         fun createUpdateTypeQuery(query: String, parameters: Map<Int, Any?> = emptyMap()): Int {
            val preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, Statement.RETURN_GENERATED_KEYS)
            try {
                if (parameters.isNotEmpty()) {
                    setParameters(preparedStatement, parameters as Map<Int, Any>)
                }
                 return preparedStatement.executeUpdate()
            } catch (e: Exception) {
                throw SQLException("Cannot execute query: Cause: ${e.cause} ; Message: ${e.message}")
            }
        }

         fun setParameters(preparesStatement: PreparedStatement, parameters: Map<Int, Any>): PreparedStatement {
            parameters.forEach { (key, value) -> preparesStatement.setObject(key, value) }
            return preparesStatement
        }
    }
}