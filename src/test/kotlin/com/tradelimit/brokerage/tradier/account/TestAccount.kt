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
