package com.github.mfamador.paymentsapi.service

import com.github.mfamador.paymentsapi.model.Operation
import com.github.mfamador.paymentsapi.repository.PaymentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class PaymentsService {

    @Autowired
    lateinit var paymentRepository: PaymentsRepository

    fun addOperation(operation: Operation): Mono<Operation> = paymentRepository.save(operation)

    fun getPayments() = "Get list payments!"
}