package com.tradelimit.brokerage.tradier

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

@ExperimentalCoroutinesApi
abstract class TradierAPITest {

    private val mainThreadSurrogate = newSingleThreadContext("Testing")

    internal val API_KEY = checkNotNull("TRADIER_TEST_ACCOUNT_ID") { "Missing api key, check enviroment for 'TRADIER_TEST_ACCOUNT_ID' "}

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


}
