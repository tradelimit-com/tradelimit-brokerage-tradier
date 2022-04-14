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
 * Get the market calendar for the current or given month. This can be used to plan ahead regarding strategies.
 * However, the Get Intraday Status should be used to determine the current status of the market.
 */
@Serializable
class MarketCalendar(val calendar: Calendar) {

    @Serializable
    data class Calendar(val month: Int, val year: Int, val days: Days)

    @Serializable
    data class Days(val day: List<Day>)

    @Serializable
    data class Day(
        val date: LocalDate,
        val status: String,
        val description: String,
        val premarket: Premarket,
        val open: Open,
        val postmarket: PostMarket
    )

    @Serializable
    data class Premarket(val start: String, val end: String)

    @Serializable
    data class Open(val start: String, val end: String)

    @Serializable
    data class PostMarket(val start: String, val end: String)
}
