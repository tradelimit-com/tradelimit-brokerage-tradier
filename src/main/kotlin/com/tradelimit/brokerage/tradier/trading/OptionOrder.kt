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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

data class OptionOrder(

    /**
     * equity symbol
     */
    @SerialName("symbol") override val symbol: String,
    /**
     * List of legs for this option order.
     */
    @SerialName("legs") val legs: List<OptionLeg>,
    /**
     * The type of order to be placed. One of: market, limit, stop, stop_limit
     */
    @SerialName("type") val type: OrderType,
    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration")  override val duration: OrderDuration,
    /**
     * Limit price. Required only for limit and stop_limit orders.
     */
    @SerialName("price") val price: Double? = null,
    /**
     * Stop price. Required only for stop and stop_limit orders.
     */
    @SerialName("stop") val stop: Double? = null,
    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") override val tag: String? = null

) : TradingOrder(symbol, duration, tag) {

    /**
     * Required value for TradingOrder type.
     */
    override val orderClass: OrderClass = OrderClass.OPTION

    companion object {
        inline fun optionOrder(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var symbol: String? = null
        var orderDuration: OrderDuration? = null
        var price: Double? = null
        var stop: Double? = null
        var tag: String? = null
        var type: OrderType? = null
        private val legs = mutableListOf<OptionLeg>()

        fun legs(lambda: LegsBuilder.() -> Unit) {
            legs.addAll(LegsBuilder().apply(lambda).build())
        }

        fun build() = OptionOrder(
            symbol = checkNotNull(symbol) { "Missing symbol parameter for order" },
            duration = checkNotNull(orderDuration) { "Missing duration parameter for order" },
            type = checkNotNull(type) { "Order type was missing for option order"},
            legs = legs,
            price = price,
            stop = stop,
            tag = tag,
        )
    }

    enum class Side {
        BUY_TO_OPEN,
        BUY_TO_CLOSE,
        SELL_TO_OPEN,
        SELL_TO_CLOSE;

        override fun toString() = name.lowercase(Locale.getDefault())
    }
}

/**
 * Legs Builder
 */
class LegsBuilder() {
    private val legs = mutableListOf<OptionLeg>()
    fun leg(lambda: OptionLeg.Builder.() -> Unit) {
        legs.add(OptionLeg.Builder().apply(lambda).build())
    }

    fun build() = legs
}


@Serializable
data class OptionLeg(
    /**
     * The side of the order. One of: buy, buy_to_cover, sell, sell_short
     */
    @SerialName("side") val side: OptionOrder.Side,
    /**
     * The number of shares ordered.
     */
    @SerialName("quantity")  val quantity: Int,
    /**
     * option_symbol	Form	String	Required	SPY140118C00195000
     */
    @SerialName("option_symbol") val optionSymbol: String

) {
    class Builder {
        var side: OptionOrder.Side? = null
        var quantity: Int? = null
        var optionSymbol: String? = null

        fun build() = OptionLeg(
            checkNotNull(side) { "Missing side for option leg" },
            checkNotNull(quantity) { "Missing quantity for option leg" },
            checkNotNull(optionSymbol) { "Missing option symbol for option leg" }
        )
    }
}
