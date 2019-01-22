package com.github.mfamador.paymentsapi

import com.github.mfamador.paymentsapi.model.Payment
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import cucumber.api.java8.En
import java.util.UUID
import kotlin.test.assertEquals

class CreationTestsSteps : En, BaseTest() {

    var count = 0L

    @Given("^a file with payment resources in json$")
    fun readResources() {
        count = service.count().block()
    }

    @When("^use them to create payment resources$")
    fun createResources() {
        paymentList().data.forEach {
            service.savePayment(Payment(UUID.randomUUID().toString(), it))!!.block()
        }
    }

    @Then("^the resources are created$")
    fun resourcesAreCreated() {
        assertEquals(14, service.count().block()-count)
    }
}