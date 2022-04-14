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

package com.tradelimit.brokerage.tradier.account

import com.tradelimit.brokerage.tradier.serialization.ZoneDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable data class History(val event: List<Event>) {

    @Serializable data class Event(
        val amount: Double,
        @SerialName("date_acquired")
        @Serializable(with = ZoneDateTimeSerializer::class)
        val date: ZonedDateTime,
        val type: AccountHistoryType

    )

    @Serializable data class Trade(
        @SerialName("commission") val commission: Double,
        @SerialName("description") val description: String,
        @SerialName("price") val price: Double,
        @SerialName("quanity") val quanity: Double,
        @SerialName("symbol") val symbol: String,
        /**
         * 	Security type of the trade (Equity, Option)
         */
        @SerialName("trade_type") val tradeType: String
    )
}


enum class AccountHistoryType(val type: String) {
    TRADE("trade"),
    OPTION("option"),
    ACH("ach"),
    WIRE("wire"),
    DIVIDEND("dividend"),
    FEE("fee"),
    TAX("tax"),
    JOURNAL("journal"),
    CHECK("check"),
    TRANSFER("transfer"),
    ADJUSTMENT("adjustment"),
    INTEREST("interest")

}
