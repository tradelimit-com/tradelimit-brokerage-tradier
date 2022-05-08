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
package com.tradelimit.brokerage.tradier.market

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OptionChains(val options: _Option) {

    @Serializable
    data class _Option(val option: List<Option>)

    @Serializable
    data class Option(
        val symbol: String,
        val description: String,
        val exch: String,
        val type: String,
        val last: Double? = null,
        val change: Double? = null,
        val volume: Long,
        val open: Double? = null,
        val high: Double? = null,
        val low: Double? = null,
        val close: Double? = null,
        val bid: Double,
        val ask: Double,
        val underlying: String,
        val strike: Double,
        @SerialName("change_percentage")
        val changePercentage: Double? = null,
        @SerialName("average_volume") val averageVolume: Double,
        @SerialName("last_volume") val lastVolume: Double,
        @SerialName("trade_date") val tradeDate: Long,
        @SerialName("prevclose") val prevClose: Double? = null,
        @SerialName("week_52_high") val week52High: Double,
        @SerialName("week_52_low") val week52Low: Double,
        @SerialName("bidsize") val bidSize: Long,
        @SerialName("bidexch") val bideExchange: String,
        @SerialName("bid_date") val bidDate: Long,
        @SerialName("asksize") val askSize: Long,
        @SerialName("askexch") val askExchange: String,
        @SerialName("ask_date") val askDate: Long,
        @SerialName("open_interest") val openInterest: Long,
        @SerialName("contract_size") val contractSize: Long,
        @SerialName("expiration_date") val expirationDate: LocalDate,
        @SerialName("expiration_type") val expirationType: String,
        @SerialName("option_type") val optionType: String,
        @SerialName("root_symbol") val rootSymbol: String,

        @SerialName("greeks") val greeks: Greeks? = null
    )
}
