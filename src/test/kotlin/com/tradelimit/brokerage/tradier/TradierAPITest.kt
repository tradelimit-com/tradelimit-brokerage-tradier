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

import com.tradelimit.brokerage.tradier.trading.TradingAPI
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach


const val TEST_URI = "http://localhost/v1"
const val TEST_TOKEN = "test_token"


@ExperimentalCoroutinesApi
abstract class TradierAPITest {

    private val mainThreadSurrogate = newSingleThreadContext("Testing")

    protected val mockEngine = MockEngine { request ->
        val response = Json.encodeToString(
            TradingAPI.OrderResponse(
                TradingAPI.OrderResponse.Order(
                    id = -1,
                    status = "OK",
                    partnerId = "123"
                )
            )
        )

        respond(
            content = ByteReadChannel(response),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    private val httpClient = HttpClient(engine = mockEngine) {

        install(ContentNegotiation) {
            json()
        }
    }

    protected val tradier = TradierClient(TEST_URI, TEST_TOKEN, httpClient)


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
