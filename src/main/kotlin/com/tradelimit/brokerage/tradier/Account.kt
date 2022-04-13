package com.tradelimit.brokerage.tradier

import com.tradelimit.brokerage.tradier.account.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory

/**
 * Fetch positions, balances and other account related details.
 */
class Account(private val tradier: TradierClient) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Tradier profile response entity.
     */
    @Serializable
    data class ProfileResponse(val profile: Profile)

    suspend fun userProfile(): Profile {
        log.debug("Making request for user profile")
        return tradier.client.get<ProfileResponse>("${tradier.apiUrl}/user/profile").profile
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
        return tradier.client.get<BalanceResponse>("${tradier.apiUrl}/accounts/${accountId}/balances").balances
    }

    /**
     * Tradier balances response entity.
     */
    @Serializable
    data class PositionsResponse(val positions: Positions)

    /**
     * Get the current positions being held in an account. These positions are updated intraday via trading.
     */
    suspend fun positions(accountId: String): Positions {
        log.debug("Making request for positions to account $accountId")
        return tradier.client.get<PositionsResponse>("${tradier.apiUrl}/accounts/{account_id}/positions").positions
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
    ): History {
        log.debug("Making request for accountHistory to account $accountId")
        val uriBuilder = URLBuilder("${tradier.apiUrl}").path("v1", "accounts", "$accountId", "history")
        val parameters = uriBuilder.parameters
        parameters.append("page", "$page")
        parameters.append("limit", "$limit")

        if (types.isNotEmpty()) {
            val typeNames = types.map { it.type }
            parameters.append("type", "$typeNames")
        }
        start?.let { parameters.append("start", "$it") }
        end?.let { parameters.append("end", "$it") }
        symbol?.let { parameters.append("symbol", "$it") }

        log.debug("Making request for accountHistory to account ${uriBuilder.buildString()}")
        return tradier.client.get<AccountHistoryResponse>(uriBuilder.buildString()).history
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
    ): GainLoss {
        log.debug("Making request for gainLoss to account $accountId")
        val uriBuilder = URLBuilder("${tradier.apiUrl}")
            .path("v1", "accounts", "$accountId", "gainloss")
        val parameters = uriBuilder.parameters
        parameters.append("page", "$page")
        parameters.append("limit", "$limit")

        sortBy?.let { parameters.append("sortBy", "$it") }
        sort?.let {parameters.append("sort", "$it")}

        start?.let { parameters.append("start", "$it") }
        end?.let { parameters.append("end", "$it") }
        symbol?.let { parameters.append("symbol", "$it") }

        return tradier.client.get<GainLossResponse>(uriBuilder.buildString()).gainLoss
    }

    @Serializable data class OrdersResponse(val orders: Orders)
    /**
     * Retrieve orders placed within an account. This API will return orders placed for the market session of the present calendar day.
     */
    suspend fun orders(accountId: String, includeTags: Boolean = false): Orders {
        return tradier.client.get<OrdersResponse>("${tradier.apiUrl}/accounts/${accountId}/orders?includeTags=${includeTags}").orders
    }

    @Serializable data class OrderResponse(val order: Orders.Order)
    /**
     * Get detailed information about a previously placed order.
     */
    suspend fun orders(accountId: String, orderId: Int, includeTags: Boolean = false): Orders.Order {
        return tradier.client.get<OrderResponse>("${tradier.apiUrl}/accounts/${accountId}/orders/${orderId}?includeTags=${includeTags}").order
    }
}
