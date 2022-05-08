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

import com.tradelimit.brokerage.tradier.serialization.ZoneDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

/**
 *
 * @see https://documentation.tradier.com/brokerage-api/reference/response/orders
 */
@Serializable
data class Orders(val orders: List<Order>) {

    @Serializable
    data class Order(
        /**
         * Unique identifier for the order
         */
        @SerialName("id") val id: Int,
        /**
         * Single-leg, One of: market, limit, stop, stop_limit
         * Multi-leg, One of: market, debit, credit, even
         */
        @SerialName("type") val type: String,
        /**
         * Security symbol or underlying security symbol
         */
        @SerialName("symbol") val symbol: String,
        /**
         * Equity, One of: buy, buy_to_cover, sell, sell_short
         * Option, One of: buy_to_open, buy_to_close, sell_to_open, sell_to_close
         */
        @SerialName("side") val side: String,
        /**
         * Number of shares or contracts
         */
        @SerialName("quantity") val quantity: Double,
        /**
         * One of: open, partially_filled, filled, expired, canceled, pending, rejected, error
         */
        @SerialName("status") val status: String,
        /**
         * 	One of: day, pre, post, gtc
         */
        @SerialName("duration") val duration: String,
        /**
         * 	Limit price
         */
        @SerialName("price") val price: Double,
        /**
         * Average fill price
         */
        @SerialName("avg_fill_price") val avgFillPrice: Double,
        /**
         * Total number of shares/contracts filled
         */
        @SerialName("exec_quantity") val execQuantity: Double,
        /**
         * 	Last fill price
         */
        @SerialName("last_fill_price") val lastFillPrice: Double,
        /**
         * 	Last fill quantity
         */
        @SerialName("last_fill_quantity") val lastFillQuantity: Double,
        /**
         * Number of shares/contracts remaining
         */
        @SerialName("remaining_quantity") val remaining_quantity: Double,
        /**
         * Date the order was created
         */
        @Serializable(with = ZoneDateTimeSerializer::class)
        @SerialName("create_date") val create_date: ZonedDateTime,
        /**
         * Date the order was last updated
         */
        @Serializable(with = ZoneDateTimeSerializer::class)
        @SerialName("transaction_date") val transaction_date: ZonedDateTime,
        /**
         * One of: equity, option, combo, multileg
         */
        @SerialName("class") val cls: String = "equity",
        /**
         * OCC option symbol
         */
        @SerialName("option_symbol") val optionSymbol: String? = null,
        /**
         * 	Stop price
         */
        @SerialName("stop_price") val stopPrice: Double? = null,
        /**
         * Rejection details
         */
        @SerialName("description") val description: String?,
        /**
         * Order tag if available
         */
        @SerialName("tag") val tag: String?,

        /**
         * List of leg orders for this order.
         */
        @SerialName("leg") val leg: List<Order>?
    )
}

