package com.tradelimit.brokerage.tradier.account

import com.tradelimit.brokerage.tradier.TestClientFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TestAccount {

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
    fun `test account returns single profile`() = runTest {
        launch(Dispatchers.Main) {
            val profile = t.account.userProfile()
            assert(profile.account.accountNumber == TestClientFactory.testAccountId)
        }
    }

    @Test
    fun `test balances`() = runTest {
        launch(Dispatchers.Main) {
            val balance = t.account.balances(TestClientFactory.testAccountId)
            assert(balance.accountNumber == TestClientFactory.testAccountId)
        }
    }

    @Test
    fun `test history`() = runTest {
        launch(Dispatchers.Main) {
            val accountHistory = t.account.accountHistory(TestClientFactory.testAccountId)
            println(accountHistory)
//            assert(balance. == TestClientFactory.testAccountId)
        }
    }
}
