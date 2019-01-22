package com.github.mfamador.paymentsapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mfamador.paymentsapi.model.ResourceList
import com.github.mfamador.paymentsapi.service.PaymentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ResourceUtils

@SpringBootTest
class BaseTest {

    @Autowired
    lateinit var service: PaymentsService

    @Autowired
    lateinit var mapper: ObjectMapper

    fun paymentList() =
        mapper.readValue(ResourceUtils.getFile("classpath:payment-list-example.json"), ResourceList::class.java)
}
