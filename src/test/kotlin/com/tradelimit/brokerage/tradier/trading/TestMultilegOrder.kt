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
class TestMultilegOrder : TradierAPITest() {

    @Test
    fun `test multileg market option order (IC)`() = runTest {
        launch(Dispatchers.Main) {
            val order = OptionOrder.optionOrder {
                symbol = "SPY"
                orderDuration = OrderDuration.DAY
                type = OrderType.MARKET
                price = 1.0
                tag = "simple spy iron condor"
                legs {
                    leg {
                        side = OptionOrder.Side.BUY_TO_OPEN
                        quantity = 1
                        optionSymbol = "SPY140118C00195000"
                    }
                    leg {
                        side = OptionOrder.Side.SELL_TO_OPEN
                        quantity = 1
                        optionSymbol = "SPY140118C00195000"
                    }
                    leg {
                        side = OptionOrder.Side.SELL_TO_OPEN
                        quantity = 1
                        optionSymbol = "SPY140118C00195000"
                    }
                    leg {
                        side = OptionOrder.Side.BUY_TO_OPEN
                        quantity = 1
                        optionSymbol = "SPY140118C00195000"
                    }
                }
            }

            tradier.trading.multiLegOrder(TEST_TOKEN, order)


            val request = mockEngine.requestHistory.first()

            // Validate url
            assert(request.url.toString() == "$TEST_URI/accounts/$TEST_TOKEN/orders")


            val formDataContent = request.body as FormDataContent
            assert(formDataContent.formData["class"] ==  "multileg")
            assert(formDataContent.formData["symbol"] ==  "SPY")
            assert(formDataContent.formData["type"] ==  "market")
            assert(formDataContent.formData["duration"] ==  "day")
            assert(formDataContent.formData["price"] ==  "1.0")
            assert(formDataContent.formData["tag"] ==  "simple spy iron condor")

            assert(formDataContent.formData["option_symbol[0]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[0]"] ==  "${OptionOrder.Side.BUY_TO_OPEN}")
            assert(formDataContent.formData["quantity[0]"] ==  "1")

            assert(formDataContent.formData["option_symbol[1]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[1]"] ==  "${OptionOrder.Side.SELL_TO_OPEN}")
            assert(formDataContent.formData["quantity[1]"] ==  "1")

            assert(formDataContent.formData["option_symbol[2]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[2]"] ==  "${OptionOrder.Side.SELL_TO_OPEN}")
            assert(formDataContent.formData["quantity[2]"] ==  "1")

            assert(formDataContent.formData["option_symbol[3]"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side[3]"] ==  "${OptionOrder.Side.BUY_TO_OPEN}")
            assert(formDataContent.formData["quantity[3]"] ==  "1")


        }
    }
}
