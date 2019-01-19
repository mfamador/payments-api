package com.github.mfamador.paymentsapi.repository

import com.github.mfamador.paymentsapi.model.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PaymentsRepository : ReactiveCrudRepository<Payment, String>