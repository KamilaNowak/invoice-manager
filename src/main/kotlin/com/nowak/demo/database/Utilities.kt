package com.nowak.demo.database

import java.sql.Date
import java.time.LocalDate

fun convertToDate(dateToConvert: LocalDate?): Date? {
    return Date.valueOf(dateToConvert)
}