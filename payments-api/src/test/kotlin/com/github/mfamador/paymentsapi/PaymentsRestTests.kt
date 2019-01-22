package com.github.mfamador.paymentsapi

import com.github.mfamador.paymentsapi.model.Payment
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentsRestTests(@Value("\${local.server.port}") private val port: Int) : BaseTest() {

    private val BASE_URI = "/v1/payments"
    private val log = LoggerFactory.getLogger(PaymentsRestTests::class.java)

    val testClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:$port")
        .build()

    @BeforeEach
    internal fun beforeEach() = log.info("beforeEach called")

    @AfterEach
    internal fun afterEach() = log.info("afterEach called")

    companion object {
        private val log = LoggerFactory.getLogger(PaymentsRestTests::class.java)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() = log.info("beforeAll called")

        @AfterAll
        @JvmStatic
        internal fun afterAll() = log.info("afterAll called")
    }

    @Test
    fun `list all payments through rest api`() {

        testClient.get().uri(BASE_URI)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `count all payments through rest api`() {

        testClient.get().uri(BASE_URI + "/count")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `the payments in file are stored correctly through rest api`() {
        val objects = paymentList()

        val count = service.count().block()

        objects.data.forEach {
            testClient.post().uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(it), Payment::class.java)
                .exchange()
                .expectStatus().isCreated
        }

        assertEquals(14, service.count().block() - count)
    }
}