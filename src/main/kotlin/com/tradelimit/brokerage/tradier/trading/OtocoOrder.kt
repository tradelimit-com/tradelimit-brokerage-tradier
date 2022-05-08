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
 * https://www.fidelity.com/webcontent/ap002390-mlo-content/19.09/help/learn_trading_conditional.shtml#whatisaonetriggersaonecancelstheotherorder
 *
 * What is a One-Triggers-a One-Cancels-the-Other (OTOCO) order?
 *
 * A One-Triggers-a One Cancels the-Other orders involves two orders-a primary order and two secondary orders. The primary order
 * may be a live order in the marketplace while the secondary orders, held in a separate order file, are not.
 * If the primary order executes in full, the secondary orders are released to the marketplace as a One-Cancels-the-Other order (OCO).
 * The execution of either leg of the OCO order triggers an attempt to cancel the unexecuted order. Partial executions
 * will also trigger an attempt to cancel the other order. An OTOCO order can be made up of stock orders, single-leg option
 * orders, or a combination of both. It is possible during volatile market conditions that both legs of an OCO could receive
 * executions. It is also possible that one order receives a delayed execution, resulting in the execution of both orders.
 *
 * Tradier Notes.
 * Please note these specific validations:
 * type must be different for both legs.
 * If both orders are equities, the symbol must be the same.
 * If both orders are options, the option_symbol must be the same.
 * If sending duration per leg, both orders must have the same duration.
 */
class OtocoOrder(
    /**
     * equity symbol
     */
    @SerialName("symbol") override val symbol: String,

    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") override val duration: OrderDuration,

    /**
     * This is the primary order that when filled will trigger the oco order secondarily.
     */
    @SerialName("order") val order: TradingOrder,

    /**
     * This is a one cancels the other order that is triggered when the first order fills.
     */
    @SerialName("oco") val oco: List<TradingOrder>,

    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") override val tag: String? = null

) : TradingOrder(symbol, duration, tag) {

    override val orderClass: OrderClass = OrderClass.OTOCO

    companion object {
        inline fun otoco(block: Builder.() -> Unit) = Builder().apply(block).build().validate()
    }

    class Builder {
        var symbol: String? = null
        var duration: OrderDuration? = null
        var tag: String? = null
        var order: TradingOrder? = null
        private var ocoOrders: List<TradingOrder> = emptyList()

        fun order(lambda: TradingOrderBuilder.() -> Unit) {
            order = TradingOrderBuilder().apply(lambda).build()
        }

        fun oco(lambda: TradingOrdersBuilder.() -> Unit) {
            ocoOrders = TradingOrdersBuilder().apply(lambda).build()
        }

        fun build() = OtocoOrder(
            symbol = checkNotNull(symbol) { "Missing symbol parameter for OTO order" },
            duration = checkNotNull(duration) { "Missing duration parameter for OTO order" },
            order = checkNotNull(order) {"Missing primary order to the initial trigger"},
            oco = ocoOrders,
            tag = tag,
        )
    }

    fun validate(): OtocoOrder {
        // If both orders are equities, the symbol must be the same.
        if (order is EquityOrder && oco[0] is EquityOrder && oco[1] is EquityOrder) {
            check(order.symbol in oco.map { it.symbol }) { "An OTOCO order containing all equity orders must have the same symbol" }
        }

        // If both orders are options, the option_symbol must be the same.
        if (order is OptionOrder && oco[0] is OptionOrder && oco[1] is OptionOrder ) {
            val l1 = (oco[0] as OptionOrder).legs.first()
            val l2 = (oco[1] as OptionOrder).legs.first()
            check(l1.optionSymbol == l2.optionSymbol) { "An OCO order containing two option orders must have the same option_symbol" }
        }

        // If sending duration per leg, both orders must have the same duration.
        check(oco[0].duration == oco[1].duration) { "An OCO order containing durations on legs must have identical durations" }
        return this
    }
}
