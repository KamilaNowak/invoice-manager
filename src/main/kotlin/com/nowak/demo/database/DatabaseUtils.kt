package com.nowak.demo.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.FileInputStream
import java.sql.Connection
import java.util.*

class DatabaseUtils {
    companion object Connector {

        private val properties = Properties()
        private val inputStream = FileInputStream("src\\main\\resources\\app.properties")
        private var dataSource: HikariDataSource
        private val config = HikariConfig()

        init {
            properties.load(inputStream)
        }

        init {
            config.jdbcUrl = properties.getProperty("db.url")
            config.username = properties.getProperty("db.username")
            config.password = properties.getProperty("db.password")
            config.driverClassName = properties.getProperty("db.driver")
            config.addDataSourceProperty("cachePrepStmts", true)
            config.addDataSourceProperty("prepStmtCacheSize",250)
            config.addDataSourceProperty("prepStmtCacheSqlLimit",2048)
            config.isAutoCommit= true
            dataSource = HikariDataSource(config)
        }

        fun getConnection(): Connection {
            return dataSource.connection
        }
    }
}