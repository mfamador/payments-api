package com.github.mfamador.paymentsapi.service

import com.github.mfamador.paymentsapi.model.Payment
import com.github.mfamador.paymentsapi.repository.PaymentsRepository
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PaymentsService {

    @Autowired
    lateinit var paymentRepository: PaymentsRepository

    fun getPayment(id: String): Mono<Payment> = paymentRepository.findById(id)

    fun getAllPayments(): Flux<Payment> = paymentRepository.findAll()

    fun count(): Mono<Long> = paymentRepository.count()

    fun savePayment(payment: Payment?): Mono<Payment>? = paymentRepository.save(payment)

    fun deletePayment(payment: Payment?): Mono<Void>  = paymentRepository.delete(payment)

    fun deletePayments(): Mono<Void>  = paymentRepository.deleteAll()
}