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
