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
