package com.github.mfamador.paymentsapi.rest

import com.github.mfamador.paymentsapi.model.Operation
import com.github.mfamador.paymentsapi.repository.PaymentsRepository
import com.github.mfamador.paymentsapi.service.PaymentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.util.*

@Component
class PaymentsHandler(val service: PaymentsService) {

    @Autowired
    lateinit var paymentRepository: PaymentsRepository

    fun getPayments(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(paymentRepository.findAll(), Operation::class.java))
    }

    fun getPayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject("get $id"))
    }

    fun addPayment(request: ServerRequest): Mono<ServerResponse> {
        val operation = request.bodyToMono(Operation::class.java)
        val id = UUID.randomUUID().toString()
        operation.map { o -> Operation(id, o.amount, o.description) }
        return created(UriComponentsBuilder.fromPath("payments/" + id).build().toUri())
            .contentType(APPLICATION_JSON)
            .body(fromPublisher(operation.flatMap { p -> paymentRepository.save(p) }, Operation::class.java))
    }

    fun updatePayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject("update $id"))
    }

    fun deletePayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject("delete $id"))
    }
}