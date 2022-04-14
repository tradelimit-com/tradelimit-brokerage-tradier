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

package com.tradelimit.brokerage.tradier.trading

import OrderDuration
import OrderSide
import OrderType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EquityOrder(
    /**
     * Account id
     */
    @SerialName("account_id") val accountId: String,
    /**
     * equity symbol
     */
    @SerialName("symbol") val symbol: String,

    /**
     * The side of the order. One of: buy, buy_to_cover, sell, sell_short
     */
    @SerialName("side") val side: OrderSide,

    @SerialName("quantity") val quantity: Int,
    /**
     * The type of order to be placed. One of: market, limit, stop, stop_limit
     */
    @SerialName("type") val type: OrderType,
    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") val duration: OrderDuration,
    @SerialName("price") val price: Double? = null,

    @SerialName("stop") val stop: String? = null,
    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") val tag: String? = null


) {


    /**
     * The kind of order to be placed.
     */
    @SerialName("class") val cls: String = OrderClass.EQUITY.cls

    private constructor(builder: Builder) : this(
        checkNotNull(builder.accountId) { "Missing cls (class) parameter for order" },
        checkNotNull(builder.symbol) { "Missing symbol parameter for order" },
        checkNotNull(builder.side) { "Missing side parameter for order" },
        checkNotNull(builder.quantity) { "Missing quanity parameter for order" },
        checkNotNull(builder.type) { "Missing type parameter for order" },
        checkNotNull(builder.duration) { "Missing duration parameter for order" },
        builder.price,
        builder.stop,
        builder.tag
    )

    companion object {
        inline fun equityOrder(block: EquityOrder.Builder.() -> Unit) = EquityOrder.Builder().apply(block).build()
    }

    class Builder {
        var accountId: String? = null
        var symbol: String? = null
        var side: OrderSide? = null
        var quantity: Int? = null
        var type: OrderType? = null
        var duration: OrderDuration? = null
        var price: Double? = null
        var stop: String? = null
        var tag: String? = null
        fun build() = EquityOrder(this)
    }
}
