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

package com.tradelimit.brokerage.tradier.trading

import com.tradelimit.brokerage.tradier.TEST_TOKEN
import com.tradelimit.brokerage.tradier.TEST_URI
import com.tradelimit.brokerage.tradier.TradierAPITest
import com.tradelimit.brokerage.tradier.trading.OcoOrder.Companion.ocoOrder
import io.ktor.client.request.forms.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TestOtocoOrder : TradierAPITest() {

    // Create an equity order and an option order
    @Test
    fun `test otoco order`() = runTest {
        launch(Dispatchers.Main) {

            tradier.trading.otocoOrder(accountId = TEST_TOKEN) {
                symbol = "SPY"
                duration = OrderDuration.GTC
                order {
                    option {
                        symbol = "SPY"
                        type = OrderType.MARKET
                        orderDuration = OrderDuration.GTC
                        legs {
                            leg {
                                side = OptionOrder.Side.BUY_TO_OPEN
                                quantity = 1
                                optionSymbol = "SPY140118C00195000"
                            }
                        }
                    }
                }
                oco {
                    option {
                        symbol = "SPY"
                        type = OrderType.MARKET
                        orderDuration = OrderDuration.GTC
                        legs {
                            leg {
                                side = OptionOrder.Side.BUY_TO_OPEN
                                quantity = 1
                                optionSymbol = "SPY140118C00195000"
                            }
                        }
                    }
                    option {
                        symbol = "SPY"
                        type = OrderType.MARKET
                        orderDuration = OrderDuration.GTC
                        legs {
                            leg {
                                side = OptionOrder.Side.BUY_TO_OPEN
                                quantity = 3
                                optionSymbol = "SPY140118C00195000"
                            }
                        }
                    }
                }
            }
            val request = mockEngine.requestHistory.first()

            val formDataContent = request.body as FormDataContent

            assertEquals("otoco", formDataContent.formData["class"], )
            assertEquals("gtc", formDataContent.formData["duration"])

            // Check option order
            assertEquals("SPY140118C00195000", formDataContent.formData["option_symbol[0]"], )
            assertEquals("${OptionOrder.Side.BUY_TO_OPEN}", formDataContent.formData["side[0]"] )
            assertEquals("1", formDataContent.formData["quantity[0]"], )
            assertEquals("gtc", formDataContent.formData["duration[0]"])

            // Check triggers order
            assertEquals( "SPY", formDataContent.formData["symbol[1]"])
            assertEquals("${OrderType.MARKET}", formDataContent.formData["type[1]"], )
            assertEquals("gtc", formDataContent.formData["duration[1]"])
            assertEquals("${OptionOrder.Side.BUY_TO_OPEN}", formDataContent.formData["side[1]"])
            assertEquals("1",formDataContent.formData["quantity[1]"])

            // Check triggers order
            assertEquals("SPY", formDataContent.formData["symbol[2]"])
            assertEquals("${OrderType.MARKET}", formDataContent.formData["type[2]"])
            assertEquals("gtc", formDataContent.formData["duration[1]"])
            assertEquals("${OptionOrder.Side.BUY_TO_OPEN}", formDataContent.formData["side[2]"])
            assertEquals("3", formDataContent.formData["quantity[2]"])


            println(mockEngine.requestHistory.first())
            assertEquals("$TEST_URI/accounts/$TEST_TOKEN/orders", request.url.toString())
        }
    }

}
