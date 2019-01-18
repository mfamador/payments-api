package com.github.mfamador.paymentsapi.service

import com.github.mfamador.paymentsapi.repository.PaymentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PaymentsService {

    @Autowired
    lateinit var paymentRepository: PaymentsRepository

    fun getPayments() = "Get list payments!"
}