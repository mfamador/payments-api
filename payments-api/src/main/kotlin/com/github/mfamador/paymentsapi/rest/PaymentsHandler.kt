package com.github.mfamador.paymentsapi.rest

import com.github.mfamador.paymentsapi.model.Payment
import com.github.mfamador.paymentsapi.service.PaymentsService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.util.*

@Component
class PaymentsHandler(val service: PaymentsService) {
    private val log = LoggerFactory.getLogger(PaymentsHandler::class.java)

    fun getPayments(request: ServerRequest): Mono<ServerResponse> {
        log.debug("get payments")
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(service.getAllPayments(), Payment::class.java))
    }

    fun count(request: ServerRequest): Mono<ServerResponse> {
        log.debug("count")
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(service.count(), Long::class.java))
    }

    fun getPayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        log.debug("get payment $id")
        val payment = service.getPayment(id)
        return payment
            .flatMap { p ->
                ok().contentType(APPLICATION_JSON)
                    .body(fromPublisher(payment, Payment::class.java))
            }
            .switchIfEmpty(notFound().build());
    }

    fun addPayment(request: ServerRequest): Mono<ServerResponse> {
        val payment = request.bodyToMono(Payment::class.java)
        val id = UUID.randomUUID().toString()
        log.debug("add payment $id $payment")
        return created(UriComponentsBuilder.fromPath("payments/").build().toUri())
            .contentType(APPLICATION_JSON)
            .body(
                fromPublisher(
                    payment.map { p -> Payment(id, p) }
                        .flatMap { p -> service.savePayment(p) }, Payment::class.java
                )
            )
    }

    fun updatePayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val payment = request.bodyToMono(Payment::class.java)
        log.debug("update payment $id $payment")
        return service.getPayment(id)
            .flatMap {
                ok().contentType(APPLICATION_JSON)
                    .body(fromPublisher(payment.flatMap { p -> service.savePayment(p) }, Payment::class.java))
            }
            .switchIfEmpty(notFound().build())
    }

    fun deletePayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        log.debug("delete payment $id")
        return service.getPayment(id)
            .flatMap { p -> noContent().build(service.deletePayment(p)) }
            .switchIfEmpty(notFound().build())
    }

    fun deletePayments(request: ServerRequest): Mono<ServerResponse> {
        log.debug("deletes payments")
        return noContent().build(service.deletePayments())
    }
}