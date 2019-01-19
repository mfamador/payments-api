package com.github.mfamador.paymentsapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mfamador.paymentsapi.model.Payment
import com.github.mfamador.paymentsapi.model.PaymentList
import com.github.mfamador.paymentsapi.service.PaymentsService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.ResourceUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentsTests(@Value("\${local.server.port}") private val port: Int) {

    private val BASE_URI = "/v1/payments"
    private val log = LoggerFactory.getLogger(PaymentsTests::class.java)

    @Autowired
    lateinit var service: PaymentsService

    @Autowired
    lateinit var mapper: ObjectMapper

    val testClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:$port")
        .build()

    @BeforeEach
    internal fun beforeEach() {
        log.info("beforeEach called")
        service.deletePayments().block()
    }

    @AfterEach
    internal fun afterEach() {
        log.info("afterEach called")
        service.deletePayments().block()
    }

    companion object {
        private val log = LoggerFactory.getLogger(PaymentsTests::class.java)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            log.info("beforeAll called")
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            log.info("afterAll called")
        }
    }

    @Test
    fun `read all payment objects from sample file`() {

        val objects =
            mapper.readValue(ResourceUtils.getFile("classpath:payment-list-example.json"), PaymentList::class.java)

        log.debug(objects.toString())
        assertEquals(14, objects.data.size)
    }

    @Test
    fun `build payment object from json`() {
        val jsonPayment = "{\n" +
                "      \"type\": \"Payment\",\n" +
                "      \"id\": \"4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43\",\n" +
                "      \"version\": 0,\n" +
                "      \"organisation_id\": \"743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb\",\n" +
                "      \"attributes\": {\n" +
                "        \"amount\": \"100.21\",\n" +
                "        \"beneficiary_party\": {\n" +
                "          \"account_name\": \"W Owens\",\n" +
                "          \"account_number\": \"31926819\",\n" +
                "          \"account_number_code\": \"BBAN\",\n" +
                "          \"account_type\": 0,\n" +
                "          \"address\": \"1 The Beneficiary Localtown SE2\",\n" +
                "          \"bank_id\": \"403000\",\n" +
                "          \"bank_id_code\": \"GBDSC\",\n" +
                "          \"name\": \"Wilfred Jeremiah Owens\"\n" +
                "        },\n" +
                "        \"charges_information\": {\n" +
                "          \"bearer_code\": \"SHAR\",\n" +
                "          \"sender_charges\": [\n" +
                "            {\n" +
                "              \"amount\": \"5.00\",\n" +
                "              \"currency\": \"GBP\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"amount\": \"10.00\",\n" +
                "              \"currency\": \"USD\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"receiver_charges_amount\": \"1.00\",\n" +
                "          \"receiver_charges_currency\": \"USD\"\n" +
                "        },\n" +
                "        \"currency\": \"GBP\",\n" +
                "        \"debtor_party\": {\n" +
                "          \"account_name\": \"EJ Brown Black\",\n" +
                "          \"account_number\": \"GB29XABC10161234567801\",\n" +
                "          \"account_number_code\": \"IBAN\",\n" +
                "          \"address\": \"10 Debtor Crescent Sourcetown NE1\",\n" +
                "          \"bank_id\": \"203301\",\n" +
                "          \"bank_id_code\": \"GBDSC\",\n" +
                "          \"name\": \"Emelia Jane Brown\"\n" +
                "        },\n" +
                "        \"end_to_end_reference\": \"Wil piano Jan\",\n" +
                "        \"fx\": {\n" +
                "          \"contract_reference\": \"FX123\",\n" +
                "          \"exchange_rate\": \"2.00000\",\n" +
                "          \"original_amount\": \"200.42\",\n" +
                "          \"original_currency\": \"USD\"\n" +
                "        },\n" +
                "        \"numeric_reference\": \"1002001\",\n" +
                "        \"payment_id\": \"123456789012345678\",\n" +
                "        \"payment_purpose\": \"Paying for goods/services\",\n" +
                "        \"payment_scheme\": \"FPS\",\n" +
                "        \"payment_type\": \"Credit\",\n" +
                "        \"processing_date\": \"2017-01-18\",\n" +
                "        \"reference\": \"Payment for Em's piano lessons\",\n" +
                "        \"scheme_payment_sub_type\": \"InternetBanking\",\n" +
                "        \"scheme_payment_type\": \"ImmediatePayment\",\n" +
                "        \"sponsor_party\": {\n" +
                "          \"account_number\": \"56781234\",\n" +
                "          \"bank_id\": \"123123\",\n" +
                "          \"bank_id_code\": \"GBDSC\"\n" +
                "        }\n" +
                "      }\n" +
                "    }"

        val payment = mapper.readValue(jsonPayment, Payment::class.java)
        println("payment = ${payment}")
    }

    @Test
    fun `the payments in file are stored correctly through service`() {
        assertEquals(0, service.count().block())

        val objects =
            mapper.readValue(ResourceUtils.getFile("classpath:payment-list-example.json"), PaymentList::class.java)

        objects.data.forEach {
            val p = service.savePayment(it)!!.block()
            log.debug("inserted ${it.id}")
        }

        assertEquals(service.count().block(), 14)
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
        val objects =
            mapper.readValue(ResourceUtils.getFile("classpath:payment-list-example.json"), PaymentList::class.java)

        objects.data.forEach {
            testClient.post().uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(it), Payment::class.java)
                .exchange()
                .expectStatus().isCreated
        }
    }
}