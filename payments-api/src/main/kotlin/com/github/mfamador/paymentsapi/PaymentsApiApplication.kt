package com.github.mfamador.paymentsapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PaymentsApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(PaymentsApiApplication::class.java, *args)
}