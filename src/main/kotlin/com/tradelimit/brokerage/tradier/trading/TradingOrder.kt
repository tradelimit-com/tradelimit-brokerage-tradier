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
import kotlinx.serialization.Serializable
import java.util.*


@Serializable
abstract class TradingOrder(
    /**
     * equity symbol
     */
    @SerialName("symbol") open val symbol: String,
    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") open val duration: OrderDuration,

    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") open val tag: String? = null

) {

    /**
     * The kind of order to be placed.
     */
    abstract val orderClass: OrderClass
}


class TradingOrdersBuilder {
    private var orders = mutableListOf<TradingOrder>()

    fun option(lambda: OptionOrder.Builder.() -> Unit) {
        orders.add(OptionOrder.Builder().apply(lambda).build())
    }

    fun equity(lambda: EquityOrder.Builder.() -> Unit) {
        orders.add(EquityOrder.Builder().apply(lambda).build())
    }

    fun build(): List<TradingOrder> {
        return orders.toList()
    }
}

class TradingOrderBuilder {
    private var order: TradingOrder? = null

    fun option(lambda: OptionOrder.Builder.() -> Unit) {
        order = OptionOrder.Builder().apply(lambda).build()
    }

    fun equity(lambda: EquityOrder.Builder.() -> Unit) {
        order = EquityOrder.Builder().apply(lambda).build()
    }

    fun build(): TradingOrder {
        return order ?: throw IllegalStateException("Attempt to call build without ever initialializing a trading order.")
    }
}


/**
 * Time the order will remain active. One of: day, gtc, pre, post
 */
@Serializable
enum class OrderDuration() {
    @SerialName("day")
    DAY,

    @SerialName("gtc")
    GTC,

    @SerialName("pre")
    PRE,

    @SerialName("post")
    POST;

    override fun toString() = name.lowercase(Locale.getDefault())
}

@Serializable
enum class OrderClass() {
    @SerialName("option")
    OPTION,

    @SerialName("equity")
    EQUITY,

    @SerialName("multileg")
    MULTILEG,

    @SerialName("combo")
    COMBO,

    @SerialName("oto")
    OTO,

    @SerialName("oco")
    OCO,

    @SerialName("OTOCO")
    OTOCO;

    override fun toString() = name.lowercase(Locale.getDefault())
}


/**
 * The type of order to be placed. One of: market, limit, stop, stop_limit
 */
@Serializable
enum class OrderType(val token: String) {
    @SerialName("market")
    MARKET("market"),

    @SerialName("limit")
    LIMIT("limit"),

    @SerialName("stop")
    STOP("stop"),

    @SerialName("stop_limit")
    STOP_LIMIT("stop_limit");

    override fun toString() = token
}
