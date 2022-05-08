/*
 * © 2022 Chris Hinshaw <chris.hinshaw@tradelimit.com>
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

import com.tradelimit.brokerage.tradier.account.AccountAPI
import com.tradelimit.brokerage.tradier.market.MarketAPI
import com.tradelimit.brokerage.tradier.trading.TradingAPI
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

/**
 * This class will
 */
class TradierClient(private val apiUrl: String, private val authToken: String, private val httpClient: HttpClient = initDefaultClient(authToken)) {

    /**
     * Fetch positions, balances and other account related details.
     */
    val account: AccountAPI by lazy {
        AccountAPI(apiUrl, httpClient)
    }

    /**
     * Fetch quotes, chains and historical data via REST and streaming APIs.
     */
    val market by lazy {
        MarketAPI(apiUrl, httpClient)
    }

    /**
     * Fetch quotes, chains and historical data via REST and streaming APIs.
     */
    val trading by lazy {
        TradingAPI(apiUrl, httpClient)
    }

    private companion object {
        /**
         * This will initialize the default http configuration based on the environment provided.
         */
        fun initDefaultClient(authToken: String) = HttpClient(CIO) {
            defaultRequest {
                header(HttpHeaders.Accept, "application/json")
                header(HttpHeaders.Authorization, "Bearer $authToken")
                header(HttpHeaders.UserAgent, "tradelimit client")
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }

            install(ContentNegotiation) {
                json()
            }
        }
    }
}
