package com.github.mfamador.paymentsapi

import com.github.mfamador.paymentsapi.model.Payment
import cucumber.api.java8.En
import java.util.UUID
import kotlin.test.assertEquals

class CreationTestsSteps : En, BaseTest() {

    var count = 0L

    init {
        Given("^a file with payment resources in json$") {
            count = service.count().block()
        }
        When("^use them to create payment resources$") {
            paymentList().data.forEach {
                service.savePayment(Payment(UUID.randomUUID().toString(), it))!!.block()
            }
        }
        Then("^the resources are created$") {
            assertEquals(14, service.count().block() - count)
        }


        Given("^a list of resources$") {
            paymentList().data.forEach {
                val p = service.savePayment(Payment(UUID.randomUUID().toString(), it))!!.block()
            }
        }
        When("^the version is set to 2$") {
            val payments = service.getAllPayments().collectList().block()
            payments.forEach {
                service.savePayment(Payment(it.id!!, 2, it))!!.block()
            }
        }
        Then("^the resources are are updated with version 2$") {
            val updatedPayments = service.getAllPayments().collectList().block()
            updatedPayments.forEach {
                assertEquals(2, it.version)
            }
        }
    }
}