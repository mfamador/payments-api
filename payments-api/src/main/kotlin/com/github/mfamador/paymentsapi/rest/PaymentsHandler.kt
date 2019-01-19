package com.github.mfamador.paymentsapi.rest

import com.github.mfamador.paymentsapi.model.Payment
import com.github.mfamador.paymentsapi.repository.PaymentsRepository
import com.github.mfamador.paymentsapi.service.PaymentsService
import org.springframework.beans.factory.annotation.Autowired
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

    fun getPayments(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(service.getAllPayments(), Payment::class.java))
    }

    fun getPayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val payment = service.getPayment(id)
        return payment
            .flatMap { p -> ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(payment, Payment::class.java)) }
            .switchIfEmpty(notFound().build());
    }

    fun addPayment(request: ServerRequest): Mono<ServerResponse> {
        val payment = request.bodyToMono(Payment::class.java)
        val id = UUID.randomUUID().toString()

        return created(UriComponentsBuilder.fromPath("payments/").build().toUri())
            .contentType(APPLICATION_JSON)
            .body(
                fromPublisher(
                    payment.map {p -> Payment(id, p)}
                        .flatMap { p -> service.savePayment(p) }, Payment::class.java))
    }

    fun updatePayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        val payment = request.bodyToMono(Payment::class.java)

        return service.getPayment(id)
            .flatMap { old ->
                ok().contentType(APPLICATION_JSON)
                    .body(fromPublisher(payment.flatMap { p -> service.savePayment(p) }, Payment::class.java))
            }
            .switchIfEmpty(notFound().build());
    }

    fun deletePayment(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")

        return service.getPayment(id)
            .flatMap { p -> noContent().build(service.deletePayment(p)) }
            .switchIfEmpty(notFound().build());
    }
}