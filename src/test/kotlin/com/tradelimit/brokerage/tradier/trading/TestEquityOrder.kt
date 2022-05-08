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
import com.tradelimit.brokerage.tradier.trading.EquityOrder.Companion.equityOrder
import io.ktor.client.request.forms.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
internal class TestEquityOrder : TradierAPITest() {

    @Test
    fun `test open equity position`() = runTest {
        launch(Dispatchers.Main) {

            val trade = tradier.trading.equityOrder(accountId = TEST_TOKEN) {
                symbol = "AMD"
                price = 1.0
                type = OrderType.LIMIT
                side = EquityOrder.Side.BUY
                quantity = 1
                duration = OrderDuration.DAY
            }

            val request = mockEngine.requestHistory.first()

            val formDataContent = request.body as FormDataContent
            assert(formDataContent.formData["symbol"] ==  "AMD")
            assert(formDataContent.formData["price"] ==  "1.0")
            assert(formDataContent.formData["type"] ==  "${OrderType.LIMIT}")

            assert(formDataContent.formData["side"] == "${EquityOrder.Side.BUY}")

            assert(formDataContent.formData["quantity"] ==  "1")
            assert(formDataContent.formData["duration"] == "${OrderDuration.DAY}")


            println(mockEngine.requestHistory.first())
            assert(request.url.toString() == "$TEST_URI/accounts/$TEST_TOKEN/orders")
            assert(trade.order.status == "OK")
        }
    }


}
