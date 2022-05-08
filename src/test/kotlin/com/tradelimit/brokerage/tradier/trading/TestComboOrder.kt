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
import com.tradelimit.brokerage.tradier.trading.ComboOrder.Companion.comboOrder
import io.ktor.client.request.forms.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
class TestComboOrder : TradierAPITest() {

    // Create an equity order and an option order
    @Test
    fun `test simple combo order`() = runTest {
        launch(Dispatchers.Main) {
            val comboOrder = comboOrder {
                symbol = "SPY"
                type = ComboOrder.Type.MARKET
                duration = OrderDuration.DAY
                legs {
                    leg {
                        side = OptionOrder.Side.BUY_TO_OPEN
                        quantity = 1
                        optionSymbol = "SPY140118C00195000"
                    }
                }
            }

            tradier.trading.comboOrder(accountId = TEST_TOKEN, comboOrder)
            val request = mockEngine.requestHistory.first()

            val formDataContent = request.body as FormDataContent

            assert(formDataContent.formData["symbol"] ==  "SPY")
            assert(formDataContent.formData["type"] ==  "market")
            assert(formDataContent.formData["duration"] ==  "day")

            assert(formDataContent.formData["option_symbol[0]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[0]"] ==  "${OptionOrder.Side.BUY_TO_OPEN}")
            assert(formDataContent.formData["quantity[0]"] ==  "1")


            println(mockEngine.requestHistory.first())
            assert(request.url.toString() == "$TEST_URI/accounts/$TEST_TOKEN/orders")
        }
    }


    @Test
    fun testMultipleOptionOrder() = runTest {

        launch(Dispatchers.Main) {
            val comboOrder = comboOrder {
                symbol = "SPY"
                type = ComboOrder.Type.MARKET
                duration = OrderDuration.DAY
                legs {
                    leg {
                        side = OptionOrder.Side.BUY_TO_OPEN
                        quantity = 1
                        optionSymbol = "SPY140118C00195000"
                    }
                    leg {
                        side = OptionOrder.Side.SELL_TO_OPEN
                        quantity = 2
                        optionSymbol = "SPY140118C00195000"
                    }
                    leg {
                        side = OptionOrder.Side.SELL_TO_OPEN
                        quantity = 3
                        optionSymbol = "SPY140118C00195000"
                    }
                }
            }

            tradier.trading.comboOrder(accountId = TEST_TOKEN, comboOrder)
            val request = mockEngine.requestHistory.first()

            val formDataContent = request.body as FormDataContent

            assert(formDataContent.formData["symbol"] ==  "SPY")
            assert(formDataContent.formData["type"] ==  "market")
            assert(formDataContent.formData["duration"] ==  "day")

            assert(formDataContent.formData["option_symbol[0]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[0]"] ==  "${OptionOrder.Side.BUY_TO_OPEN}")
            assert(formDataContent.formData["quantity[0]"] ==  "1")

            assert(formDataContent.formData["option_symbol[1]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[1]"] ==  "${OptionOrder.Side.SELL_TO_OPEN}")
            assert(formDataContent.formData["quantity[1]"] ==  "2")

            assert(formDataContent.formData["option_symbol[2]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[2]"] ==  "${OptionOrder.Side.SELL_TO_OPEN}")
            assert(formDataContent.formData["quantity[2]"] ==  "3")

            assert(request.url.toString() == "$TEST_URI/accounts/$TEST_TOKEN/orders")
        }
    }

}
