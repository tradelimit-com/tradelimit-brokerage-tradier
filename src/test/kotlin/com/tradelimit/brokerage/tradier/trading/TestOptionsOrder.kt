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
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TestOptionsOrder : TradierAPITest() {

    @Test
    fun testSingleOptionOrder() = runTest {
        launch(Dispatchers.Main) {
            val order = OptionOrder.optionOrder {
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

            tradier.trading.optionOrder(TEST_TOKEN, order)
            val request = mockEngine.requestHistory.first()

            val formDataContent = request.body as FormDataContent
            assert(formDataContent.formData["symbol"] ==  "SPY")
            assert(formDataContent.formData["option_symbol"] ==  "SPY140118C00195000")
            assert(formDataContent.formData["side"] ==  "${OptionOrder.Side.BUY_TO_OPEN}")


            assert(request.url.toString() == "$TEST_URI/accounts/$TEST_TOKEN/orders")
        }
    }

}
