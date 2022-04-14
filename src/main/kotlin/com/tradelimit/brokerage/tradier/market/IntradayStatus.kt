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
