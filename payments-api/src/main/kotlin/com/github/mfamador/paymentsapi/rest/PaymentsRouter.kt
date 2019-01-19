package com.github.mfamador.paymentsapi.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
class PaymentsRouter(private val handler: PaymentsHandler) {

    @Bean
    fun route() = router {
        accept(APPLICATION_JSON).nest { "/v1/payments".nest {
                GET("/", handler::getPayments)
                POST("/", handler::addPayment)
                PUT("/{id}", handler::updatePayment)
                GET("/{id}", handler::getPayment)
                DELETE("/{id}", handler::deletePayment)
            }
        }
    }
}