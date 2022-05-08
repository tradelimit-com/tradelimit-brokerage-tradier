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
import io.ktor.client.request.forms.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TestOtoOrder : TradierAPITest()  {

    // Create an equity order and an option order
    @Test
    fun `test oto order`() = runTest {
        launch(Dispatchers.Main) {

            val trade = tradier.trading.otoOrder(accountId = TEST_TOKEN)  {
                symbol = "SPY"
                duration = OrderDuration.DAY
                order {
                    option {
                        symbol = "SPY"
                        type = OrderType.MARKET
                        orderDuration = OrderDuration.DAY
                        legs {
                            leg {
                                side = OptionOrder.Side.BUY_TO_OPEN
                                quantity = 1
                                optionSymbol = "SPY140118C00195000"
                            }
                        }
                    }
                }
                triggers {
                    equity {
                        symbol = "SPY"
                        type = OrderType.MARKET
                        duration = OrderDuration.DAY
                        side = EquityOrder.Side.BUY
                        quantity = 50
                    }
                }
            }
            val request = mockEngine.requestHistory.first()

            val formDataContent = request.body as FormDataContent

            assert(formDataContent.formData["class"] == "oto")
            assert(formDataContent.formData["duration"] ==  "day")

            // Check option order
            assert(formDataContent.formData["option_symbol[0]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[0]"] ==  "${OptionOrder.Side.BUY_TO_OPEN}")
            assert(formDataContent.formData["quantity[0]"] ==  "1")

            // Check triggers order
            assert(formDataContent.formData["symbol[1]"] ==  "SPY")
            assert(formDataContent.formData["type[1]"] ==  "${OrderType.MARKET}")
            assert(formDataContent.formData["duration[1]"] ==  "${OrderDuration.DAY}")
            assert(formDataContent.formData["side[1]"] ==  "${EquityOrder.Side.BUY}")
            assert(formDataContent.formData["quantity[1]"] ==  "50")


            println(mockEngine.requestHistory.first())
            assert(request.url.toString() == "$TEST_URI/accounts/$TEST_TOKEN/orders")
        }
    }

}
