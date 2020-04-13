package com.nowak.demo.database

import org.mindrot.jbcrypt.BCrypt
import sun.net.idn.StringPrep
import java.sql.Date
import java.time.LocalDate

fun convertToDate(dateToConvert: LocalDate?): Date? {
    return Date.valueOf(dateToConvert)
}

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password,BCrypt.gensalt())
}
fun verifyPassword(plainPassword: String, hashPassword:String): Boolean{
    return BCrypt.checkpw(plainPassword, hashPassword)
}