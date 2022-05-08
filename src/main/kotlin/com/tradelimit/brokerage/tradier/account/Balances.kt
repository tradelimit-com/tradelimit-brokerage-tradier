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
package com.tradelimit.brokerage.tradier.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.lang.IllegalStateException



@Serializable data class Balances(
    @SerialName("option_short_value") val optionShortValue: Double,
    @SerialName("total_equity") val totalEquity: Double,
    @SerialName("account_number") val accountNumber: String,
    @SerialName("account_type") val accountType: String,
    @SerialName("close_pl") val closePL: Double,
    @SerialName("current_requirement") val currentRequirement: Double,
    @SerialName("equity") val equity: Double,
    @SerialName("long_market_value") val longMarketValue: Double,
    @SerialName("market_value") val marketValue: Double,
    @SerialName("open_pl") val openPL: Double,
    @SerialName("option_long_value") val optionLongValue: Double,
    @SerialName("option_requirement") val optionRequirement: Double,
    @SerialName("pending_orders_count") val pendingOrdersCount: Double,
    @SerialName("short_market_value") val shortMarketValue: Double,
    @SerialName("stock_long_value") val stockLongValue: Double,
    @SerialName("total_cash") val totalCash: Double,
    @SerialName("uncleared_funds") val unclearedFunds: Double,
    @SerialName("pending_cash") val pendingCash: Double,

    @SerialName("margin") val margin: Margin? = null,
    @SerialName("cash") val cash: Cash? = null,
    @SerialName("pdt") val pdt: PDT? = null,

) {

    enum class AccountType {
        MARGIN,
        CASH,
        PDT
    }


    /**
     * Helper in case you don't know what kind of account you have.
     */
    fun getAccountType(): AccountType {
        if (margin != null) return AccountType.MARGIN
        if (cash != null) return AccountType.CASH
        if (pdt != null) return AccountType.MARGIN
        throw IllegalStateException("All account types were null for margin, cash, pdt")
    }

    /**
     *   "margin": {
     *      "fed_call": 0,
     *      "maintenance_call": 0,
     *      "option_buying_power": 6363.860000000000000000000000,
     *      "stock_buying_power": 12727.7200000000000000,
     *      "stock_short_value": 0,
     *      "sweep": 0
     *     }
     */
    @Serializable data class Margin(
        @SerialName("fed_call") val fedCall: Double,
        @SerialName("maintenance_call") val maintenanceCall: Double,
        @SerialName("option_buying_power") val optionBuyingPower: Double,
        @SerialName("stock_buying_power") val stockBuyingPower: Double,
        @SerialName("stock_short_value") val stockShortValue: Double,
        @SerialName("sweep") val sweep: Double,
    )

    /**
     *
     * "cash": {
     *  "cash_available": 4343.38000000,
     *  "sweep": 0,
     *  "unsettled_funds": 1310.00000000
     *  }
     */
    @Serializable data class Cash(
        @SerialName("fed_call") val fedCall: Double,
        @SerialName("maintenance_call") val maintenanceCall: Double,
        @SerialName("option_buying_power") val optionBuyingPower: Double,
        @SerialName("stock_buying_power") val stockBuyingPower: Double,
        @SerialName("stock_short_value") val stockShortValue: Double,
    )


    @Serializable data class PDT(
        @SerialName("fed_call") val fedCall: Double,
        @SerialName("maintenance_call") val maintenanceCall: Double,
        @SerialName("option_buying_power") val optionBuyingPower: Double,
        @SerialName("stock_buying_power") val stockBuyingPower: Double,
        @SerialName("stock_short_value") val stockShortValue: Double,
    )
}
