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

package com.tradelimit.brokerage.tradier.market

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * {
 *   "symbol": "AAPL",
 *   "description": "Apple Inc",
 *   "exch": "Q",
 *   "type": "stock",
 *   "last": 208.21,
 *   "change": -3.54,
 *   "volume": 25288395,
 *   "open": 204.29,
 *   "high": 208.71,
 *   "low": 203.5,
 *   "close": null,
 *   "bid": 208.19,
 *   "ask": 208.21,
 *   "change_percentage": -1.68,
 *   "average_volume": 27215269,
 *   "last_volume": 100,
 *   "trade_date": 1557168406000,
 *   "prevclose": 211.75,
 *   "week_52_high": 233.47,
 *   "week_52_low": 142.0,
 *   "bidsize": 12,
 *   "bidexch": "Q",
 *   "bid_date": 1557168406000,
 *   "asksize": 1,
 *   "askexch": "Y",
 *   "ask_date": 1557168406000,
 *   "root_symbols": "AAPL"
 * }
 */
@Serializable
data class Quote(
    val symbol: String,
    val description: String,
    val exch: String,
    val type: String,
    val last: Double,
    val change: Double,
    val volume: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val bid: Double,
    val ask: Double,
    @SerialName("change_percentage")
    val changePercentage: Double,
    @SerialName("average_volume")
    val averageVolume: Double,
    @SerialName("last_volume")
    val lastVolume: Long,
    @SerialName("trade_date")
    val tradeDate: Long,
    @SerialName("prevclose")
    val prevClose: Double,
    @SerialName("week_52_high")
    val week52High: Double,
    @SerialName("week_52_low")
    val week52Low: Double,
    val bidsize: Long,
    val bidexch: String,
    @SerialName("bid_date")
    val bidDate: Long,
    val asksize: Long,
    val askexch: String,
    @SerialName("ask_date")
    val askDate: Long,
    @SerialName("root_symbols")
    val rootSymbols: String
) {
}
