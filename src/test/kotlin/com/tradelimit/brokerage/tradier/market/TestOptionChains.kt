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
