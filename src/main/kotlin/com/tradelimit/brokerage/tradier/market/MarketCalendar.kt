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
