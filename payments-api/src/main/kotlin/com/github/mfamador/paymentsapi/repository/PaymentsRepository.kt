package com.github.mfamador.paymentsapi.repository

import com.github.mfamador.paymentsapi.model.Operation
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PaymentsRepository : ReactiveCrudRepository<Operation, String>