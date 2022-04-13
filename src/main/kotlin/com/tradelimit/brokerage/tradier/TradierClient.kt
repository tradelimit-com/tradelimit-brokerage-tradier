package com.tradelimit.brokerage.tradier

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

class TradierClient(val apiUrl: String, val authToken: String) {

    internal val client = HttpClient(CIO) {
        defaultRequest {
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.Authorization, "Bearer $authToken")
            header(HttpHeaders.UserAgent, "tradelimit client")
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    /**
     * Fetch positions, balances and other account related details.
     */
    val account: Account by lazy {
        Account(this)
    }

    /**
     * Fetch quotes, chains and historical data via REST and streaming APIs.
     */
    val market by lazy {
        Market(this)
    }

    /**
     * Fetch quotes, chains and historical data via REST and streaming APIs.
     */
    val trading by lazy {
        Trading(this)
    }
}
