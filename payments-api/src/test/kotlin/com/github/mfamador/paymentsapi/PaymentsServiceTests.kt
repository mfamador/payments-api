package com.github.mfamador.paymentsapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mfamador.paymentsapi.model.Payment
import com.github.mfamador.paymentsapi.model.ResourceList
import com.github.mfamador.paymentsapi.service.PaymentsService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.ResourceUtils
import reactor.core.publisher.Mono
import java.util.UUID
import kotlin.test.Ignore
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentsServiceTests : BaseTest() {

    private val log = LoggerFactory.getLogger(PaymentsServiceTests::class.java)

    @BeforeEach
    internal fun beforeEach() = log.info("beforeEach called")

    @AfterEach
    internal fun afterEach() = log.info("afterEach called")

    companion object {
        private val log = LoggerFactory.getLogger(PaymentsServiceTests::class.java)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() = log.info("beforeAll called")

        @AfterAll
        @JvmStatic
        internal fun afterAll() = log.info("afterAll called")
    }

    @Test
    fun `read all payment objects from sample file`() =
        assertEquals(14, paymentList().data.size)

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
        val count = service.count().block()

        paymentList().data.forEach {
            val p = service.savePayment(Payment(getId(), it))!!.block()
        }

        assertEquals(14, service.count().block() - count)
    }

    @Test
    fun `the payments in file are updated correctly through service`() {
        val count = service.count().block()

        paymentList().data.forEach {
            val p = service.savePayment(Payment(getId(), it))!!.block()
        }

        assertEquals(14, service.count().block() - count)

        val payments = service.getAllPayments().collectList().block()
        payments.forEach {
            service.savePayment(Payment(it.id!!, 2, it))!!.block()
        }

        val updatedPayments = service.getAllPayments().collectList().block()
        updatedPayments.forEach {
            assertEquals(2, it.version)
        }

        assertEquals(14, service.count().block() - count)
    }

    @Test
    fun `the payments in file are deleted correctly after created`() {
        val count = service.count().block()

        val list = mutableListOf<Payment>()
        paymentList().data.forEach {
            val p = service.savePayment(Payment(getId(), it))!!.block()
            list.add(p)
        }

        assertEquals(14, service.count().block() - count)

        list.forEach {
            service.deletePayment(it).block()
        }

        assertEquals(0, service.count().block() - count)
    }

    @Ignore
    @Test
    fun `delete all payments through service`() {

        service.deletePayments().block()
        val count = service.count().block()
        assertEquals(0, count)
    }

    private fun getId() = UUID.randomUUID().toString()

}