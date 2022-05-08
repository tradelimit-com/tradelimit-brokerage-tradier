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
import kotlinx.serialization.Serializable


/**
 * This is the defined time periods for query.
 *  <pre>
 *  Interval	Data Available (Open)	Data Available (All)
 *  tick	    5 days	                N/A
 *  1min	    20 days	                10 days
 *  5min	    40 days	                18 days
 *  15min	    40 days	                18 days
 * </pre>
 */
@Serializable
enum class Interval(name: String) {
    Tick("tick"),
    Min1("1min"),
    Min5("1min"),
    Min15("1min"),
    Daily("daily")
}

@Serializable
class Historical(val history: List<Period>) {

    @Serializable
    data class Period(
        val date: LocalDate,
        val open: Double,
        val high: Double,
        val low: Double,
        val close: Double,
        val volume: Long
    )
}
