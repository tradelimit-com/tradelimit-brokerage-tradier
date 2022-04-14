/*
 * Copyright 2022 tradelimit.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
