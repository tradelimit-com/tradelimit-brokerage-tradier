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

package com.tradelimit.brokerage.tradier.market

import com.tradelimit.brokerage.tradier.TestClientFactory
import com.tradelimit.brokerage.tradier.TradierClient
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TestOptionChains {

    private val mainThreadSurrogate = newSingleThreadContext("Testing")
    private val t = TestClientFactory.testClient()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


    @Test
    fun testQuotes() = runTest {
        launch(Dispatchers.Main) {
            val amd = t.market.quotes(listOf("AMD", "QQQ"))
            println(amd)
        }
    }

    @Test
    fun `test options chain`() = runTest {
        launch(Dispatchers.Main) {
            val expirations = t.market.optionExpiration(symbol = "VXX")
//            val chains = t.market.optionChain("VXX", expiration = expirations.expirations.date)
//            println(chains)
        }
    }
}
