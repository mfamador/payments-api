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
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.util.UUID

@Component

class PaymentsHandler(val service: PaymentsService) {
    private val log = LoggerFactory.getLogger(PaymentsHandler::class.java)

    fun getPayments(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(service.getAllPayments(), Payment::class.java))

    fun count(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(service.count(), Long::class.java))

    fun getPayment(request: ServerRequest): Mono<ServerResponse> =
        service.getPayment(getId(request))
            .flatMap { ok().contentType(APPLICATION_JSON).syncBody(it) }
            .switchIfEmpty(notFound().build());

    fun addPayment(request: ServerRequest): Mono<ServerResponse> =
        created(UriComponentsBuilder.fromPath("payments/").build().toUri())
            .contentType(APPLICATION_JSON)
            .body(fromPublisher(request.bodyToMono(Payment::class.java)
                .map { p -> Payment(UUID.randomUUID().toString(), p) }
                .flatMap { p -> service.savePayment(p) }, Payment::class.java))

    fun updatePayment(request: ServerRequest): Mono<ServerResponse> =
        service.getPayment(getId(request))
            .flatMap {
                ok().contentType(APPLICATION_JSON)
                    .body(fromPublisher(request.bodyToMono(Payment::class.java)
                        .flatMap { p -> service.savePayment(p) }, Payment::class.java)) }
            .switchIfEmpty(notFound().build())

    fun deletePayment(request: ServerRequest): Mono<ServerResponse> =
        service.getPayment(getId(request))
            .flatMap { p -> noContent().build(service.deletePayment(p)) }
            .switchIfEmpty(notFound().build())

    fun deletePayments(request: ServerRequest): Mono<ServerResponse> = noContent().build(service.deletePayments())

    private fun getId(request: ServerRequest) = request.pathVariable("id")
}