package com.github.mfamador.paymentsapi.model

import org.springframework.data.annotation.Id
import java.text.SimpleDateFormat
import java.util.*

data class Operation(@Id val id: String?,
                     val amount: Int = 0,
                     val description: String)

data class Event(val time: String, val amount: Int, val description: String) {
    constructor(amount: Int, description: String) : this(dateFormat.format(Date()), amount, description)

    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }
}