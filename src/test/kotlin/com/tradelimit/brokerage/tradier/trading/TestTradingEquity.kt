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

import OrderDuration
import OrderSide
import com.tradelimit.brokerage.tradier.TradierAPITest
import com.tradelimit.brokerage.tradier.TestClientFactory
import com.tradelimit.brokerage.tradier.trading.EquityOrder.Companion.equityOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class TestTradingEquity : TradierAPITest() {

    private val tradier = TestClientFactory.testClient()

    @Test
    fun `test open equity position`() = runTest {
        launch(Dispatchers.Main) {
            val order = equityOrder {
                accountId = API_KEY
                symbol = "AMD"
                price = 1.0
                type = OrderType.LIMIT
                side = OrderSide.BUY
                quantity = 1
                duration = OrderDuration.DAY
            }

            val trade = tradier.trading.equityOrder(order)
            assert(trade.order.status == "OK")
        }
    }


}
