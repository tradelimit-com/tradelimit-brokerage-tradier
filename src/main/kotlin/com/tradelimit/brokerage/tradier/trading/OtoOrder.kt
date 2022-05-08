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
 * Place a one-triggers-other order. This order type is composed of two separate orders sent simultaneously. The property keys of each order are indexed.
 */
class OtoOrder(
    /**
     * equity symbol
     */
    @SerialName("symbol") override val symbol: String,

    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") override val duration: OrderDuration,

    @SerialName("order") val order: TradingOrder? = null,

    @SerialName("triggers") val triggers: TradingOrder? = null,

    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") override val tag: String? = null,

    ) : TradingOrder(symbol, duration, tag) {

    override val orderClass: OrderClass = OrderClass.OTO

    companion object {
        inline fun otoOrder(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var symbol: String? = null
        var duration: OrderDuration? = null
        var order: TradingOrder? = null
        var triggers: TradingOrder? = null
        var tag: String? = null

        fun order(lambda: TradingOrderBuilder.() -> Unit) {
            order = TradingOrderBuilder().apply(lambda).build()
        }

        fun triggers(lambda: TradingOrderBuilder.() -> Unit) {
            triggers = TradingOrderBuilder().apply(lambda).build()
        }

        fun build() = OtoOrder(
            symbol = checkNotNull(symbol) { "Missing symbol parameter for OTO order" },
            duration = checkNotNull(duration) { "Missing duration parameter for OTO order" },
            order = checkNotNull(order) { "Missing primary order for OTO order" },
            triggers = checkNotNull(triggers) { " Missing triggers order for OTO order" },
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

        fun build() : TradingOrder {
            return checkNotNull(order)
        }
    }
}
