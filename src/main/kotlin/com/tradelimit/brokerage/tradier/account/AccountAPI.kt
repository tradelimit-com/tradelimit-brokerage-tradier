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
package com.tradelimit.brokerage.tradier.account


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory

/**
 * Fetch positions, balances and other account related details.
 */
class AccountAPI(val apiUrl: String, private val httpClient: HttpClient) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Tradier profile response entity.
     */
    @Serializable
    data class ProfileResponse(val profile: Profile)

    suspend fun userProfile(): Profile {
        log.debug("Making request for user profile")
        return httpClient.get("${apiUrl}/user/profile").body()
    }

    /**
     * Tradier balances response entity.
     */
    @Serializable
    data class BalanceResponse(val balances: Balances)

    /**
     * This will return you the balance on
     */
    suspend fun balances(accountId: String): Balances {
        log.debug("Making request for balances to account $accountId")
        return httpClient.get("${apiUrl}/accounts/${accountId}/balances").body()
    }

    /**
     * Tradier balances response entity.
     */
    @Serializable
    data class PositionsResponse(val positions: Positions)

    /**
     * Get the current positions being held in an account. These positions are updated intraday via trading.
     */
    suspend fun positions(accountId: String): PositionsResponse {
        log.debug("Making request for positions to account $accountId")
        return httpClient.get("${apiUrl}/accounts/{account_id}/positions").body()
    }

    @Serializable
    data class AccountHistoryResponse(val history: History)

    /**
     * Get historical activity for an account. This data originates with our clearing firm and inherently has a few limitations:
     *
     * Updated nightly (not intraday)
     * Will not include specific time (hours/minutes) a position or order was created or closed
     * Will not include order numbers
     */
    suspend fun accountHistory(
        accountId: String,
        page: Int = 1,
        limit: Int = 25,
        vararg types: AccountHistoryType,
        start: LocalDate? = null,
        end: LocalDate? = null,
        symbol: String? = null
    ): AccountHistoryResponse {
        log.debug("Making request for accountHistory to account $accountId")

        return httpClient.get("$apiUrl/accounts/$accountId/history") {
            parameter("page", "$page")
            parameter("limit", "$limit")
            symbol?.let { parameter("symbol", "$it") }
            start?.let { parameter("start", "$it") }
            end?.let { parameter("end", "$it") }

            types.forEach { parameter("type", it.type) }
        }.body()
    }

    @Serializable
    data class GainLossResponse(@SerialName("gainloss") val gainLoss: GainLoss)

    /**
     * Get cost basis information for a specific user account. This includes information for all closed positions.
     * Cost basis information is updated through a nightly batch reconciliation process with our clearing firm.
     */
    suspend fun gainLoss(
        accountId: String,
        page: Int = 1,
        limit: Int = 100,
        sortBy: LocalDate? = null,
        sort: String? = null,
        start: LocalDate? = null,
        end: LocalDate? = null,
        symbol: String? = null
    ): GainLossResponse {
        log.debug("Making request for gainLoss to account $accountId")

        return httpClient.get("$apiUrl/accounts/$accountId/gainloss") {
            parameter("page", "$page")
            parameter("limit", "$limit")
            sortBy?.let { parameter("sortBy", "$it") }
            sort?.let { parameter("sort", "$it") }

            start?.let { parameter("start", "$it") }
            end?.let { parameter("end", "$it") }
            symbol?.let { parameter("symbol", "$it") }
        }.body()
    }

    @Serializable
    data class OrdersResponse(val orders: Orders)

    /**
     * Retrieve orders placed within an account. This API will return orders placed for the market session of the present calendar day.
     */
    suspend fun orders(accountId: String, includeTags: Boolean = false): OrdersResponse {
        return httpClient.get("${apiUrl}/accounts/${accountId}/orders?includeTags=${includeTags}").body()
    }

    @Serializable
    data class OrderResponse(val order: Orders.Order)

    /**
     * Get detailed information about a previously placed order.
     */
    suspend fun orders(accountId: String, orderId: Int, includeTags: Boolean = false): OrderResponse {
        return httpClient.get("${apiUrl}/accounts/${accountId}/orders/${orderId}?includeTags=${includeTags}").body()
    }
}
