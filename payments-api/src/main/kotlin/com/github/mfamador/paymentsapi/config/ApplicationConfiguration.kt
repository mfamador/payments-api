package com.github.mfamador.paymentsapi.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableReactiveMongoRepositories
class ApplicationConfiguration : AbstractReactiveMongoConfiguration() {

    @Bean
    fun mongoEventListener(): LoggingEventListener {
        return LoggingEventListener()
    }

    @Bean
    override fun reactiveMongoClient(): MongoClient {
        return MongoClients.create()
    }

    override fun getDatabaseName(): String {
        return "payments"
    }
}