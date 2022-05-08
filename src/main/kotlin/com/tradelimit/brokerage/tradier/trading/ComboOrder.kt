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

package com.tradelimit.brokerage.tradier.trading

import kotlinx.serialization.SerialName
import java.util.*


/**
 * Place a combo order. This is a specialized type of order consisting of one equity leg and one option leg.
 * It can optionally include a second option leg, for some strategies.
 */
data class ComboOrder(
    /**
     * equity symbol
     */
    @SerialName("symbol") override val symbol: String,

    /**
     * The type of order to be placed. One of: market, debit, credit, even
     */
    @SerialName("type") val type: Type,
    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") override val duration: OrderDuration,

    /**
     * Optional Limit price. Required only for limit and stop_limit orders.
     */
    @SerialName("price") val price: Double? = null,

    /**
     * Collection of option legs for this combo order. The max is 3 option legs for a single combo order.
     */
    @SerialName("optionLegs") val optionLegs: List<OptionLeg>,
    /**
     * Order tag.
     * Maximum length of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") override val tag: String? = null

) : TradingOrder(symbol, duration, tag) {

    override val orderClass = OrderClass.COMBO

    // Entry for the combo order builder
    companion object {
        inline fun comboOrder(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    enum class Type {
        MARKET, DEBIT, CREDIT, EVEN;
        override fun toString() = name.lowercase(Locale.getDefault())
    }

    class Builder {
        var symbol: String? = null
        var type: Type? = null
        var duration: OrderDuration? = null
        var price: Double? = null
        private val legs = mutableListOf<OptionLeg>()
        var tag: String? = null

        fun legs(lambda: LegsBuilder.() -> Unit) {
            legs.addAll(LegsBuilder().apply(lambda).build())
        }

        fun build(): ComboOrder {
            check(legs.size in 1..3) { "combo option legs count should be 1-3 legs" }

            return ComboOrder(
                symbol = checkNotNull(symbol) { "Missing symbol parameter for order" },
                duration = checkNotNull(duration) { "Missing duration parameter for order" },
                type = checkNotNull(type) { "Missing type for combo order must be of type (MARKET, DEBIT, CREDIT, EVEN)" },
                price = price,
                tag = tag,
                optionLegs = legs.toList()
            )
        }
    }
}

