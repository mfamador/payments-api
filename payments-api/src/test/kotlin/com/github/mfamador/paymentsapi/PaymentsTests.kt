package com.github.mfamador.paymentsapi

import com.github.mfamador.paymentsapi.model.Attributes
import com.github.mfamador.paymentsapi.model.Party
import com.github.mfamador.paymentsapi.model.Payment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentsTests(@Value("\${local.server.port}") private val port: Int) {

    val testClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:$port")
        .build()

    @Test
    fun testGetOperations() {

        testClient.get().uri("/payments")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun testPostOperation() {
        val request = Payment(UUID.randomUUID(), 1, UUID.randomUUID(), Attributes(232.3, Party("test_party")), "test payment #1")

        testClient.post().uri("/payments")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(request), Payment::class.java)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `adds an amount and the value is added to totalAmount`() {


    }
}