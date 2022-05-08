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

package com.tradelimit.brokerage.tradier

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream


@Serializable data class TestCredentials(val sanboxUri: String, val accountId: String, val token: String)


// Might be overkill to create a factory but this should create you a new client
// per test case.
object TestClientFactory  {
    private val tc by lazy {
        val sanboxUri = checkNotNull(System.getenv("TRADIER_TEST_URI")) { "Missing environment variable TRADIER_TEST_URI "}
        val accountId = checkNotNull(System.getenv("TRADIER_TEST_ACCOUNT_ID")) { "Missing environment variable TRADIER_TEST_ACCOUNT_ID "}
        val token = checkNotNull(System.getenv("TRADIER_TEST_TOKEN")) { "Missing environment variable TRADIER_TEST_TOKEN "}
        TestCredentials(sanboxUri, accountId, token)
    }

    fun testClient(): TradierClient {
        checkNotNull(tc.accountId)  {" Account id was null for test environment"}
        checkNotNull(tc.token)  {" Token was null for test environment"}
        return TradierClient(tc.sanboxUri, tc.token)
    }

    val testAccountId = tc.accountId
}
