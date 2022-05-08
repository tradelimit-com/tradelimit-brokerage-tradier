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

/**
 * A one-cancels-the-other (OCO) order is a pair of conditional orders stipulating that if one order executes, then
 * the other order is automatically canceled. An OCO order often combines a stop order with a limit order on an automated
 * trading platform.When either the stop or limit price is reached and the order is executed, the other order is automatically
 * canceled. Experienced traders use OCO orders to mitigate risk and enter the market.
 *
 * Tradier Notes.
 * Please note these specific validations:
 * type must be different for both legs.
 * If both orders are equities, the symbol must be the same.
 * If both orders are options, the option_symbol must be the same.
 * If sending duration per leg, both orders must have the same duration.
 */
class OcoOrder(
    /**
     * equity symbol
     */
    @SerialName("symbol") override val symbol: String,

    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") override val duration: OrderDuration,

    @SerialName("orders") val orders: List<TradingOrder> = emptyList(),

    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") override val tag: String? = null,

    ) : TradingOrder(symbol, duration, tag) {

    override val orderClass: OrderClass = OrderClass.OCO

    companion object {
        inline fun ocoOrder(block: Builder.() -> Unit) = Builder().apply(block).build().validate()
    }

    class Builder {
        var symbol: String? = null
        var duration: OrderDuration? = null
        var tag: String? = null
        private val orders = mutableListOf<TradingOrder>()

        fun order(lambda: TradingOrderBuilder.() -> Unit) {
            orders.add(TradingOrderBuilder().apply(lambda).build())
        }

        fun build() = OcoOrder(
            symbol = checkNotNull(symbol) { "Missing symbol parameter for OTO order" },
            duration = checkNotNull(duration) { "Missing duration parameter for OTO order" },
            orders = orders,
            tag = tag,
        )
    }

    class TradingOrderBuilder {
        var order: TradingOrder? = null

        fun option(lambda: OptionOrder.Builder.() -> Unit) {
            order = OptionOrder.Builder().apply(lambda).build()
        }

        fun equity(lambda: EquityOrder.Builder.() -> Unit) {
            order = EquityOrder.Builder().apply(lambda).build()
        }

        fun build(): TradingOrder {
            return checkNotNull(order)
        }
    }

    fun validate() : OcoOrder {
        check(orders.size == 2) {"An OCO order should have exactly two orders."}

        // If both orders are equities, the symbol must be the same.
        if (orders[0] is EquityOrder && orders[1] is EquityOrder) {
            check(orders[0].symbol == orders[1].symbol) { "An OCO order containing two equity orders must have the same symbol"}
        }

        // If both orders are options, the option_symbol must be the same.
        if (orders[0] is OptionOrder && orders[1] is OptionOrder) {
            val l1 = (orders[0] as OptionOrder).legs.first()
            val l2 = (orders[0] as OptionOrder).legs.first()
            check(l1.optionSymbol == l2.optionSymbol) { "An OCO order containing two option orders must have the same option_symbol" }
        }

        // If sending duration per leg, both orders must have the same duration.
        check(orders[0].duration == orders[1].duration) { "An OCO order containing durations on legs must have identical durations"}

        return this
    }
}
