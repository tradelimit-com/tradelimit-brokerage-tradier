package com.tradelimit.brokerage.tradier.market

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Get the intraday market status. This call will change and return information pertaining to the current day.
 * If programming logic on whether the market is open/closed – this API call should be used to determine the current state.
 *
 * {
 *  "clock": {
 *  "date": "2019-05-06",
 *  "description": "Market is open from 09:30 to 16:00",
 *  "state": "open",
 *  "timestamp": 1557156988,
 *  "next_change": "16:00",
 *  "next_state": "postmarket"
 *  }
 * }
 */
@Serializable
data class IntradayStatus(val clock: Clock) {

    @Serializable
    data class Clock(
        val date: LocalDate,
        val description: String,
        val state: String,
        val timestamp: Long,
        val nextChange: String,
        val nextState: String
    )
}
