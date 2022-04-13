package com.tradelimit.brokerage.tradier.market

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


/**
 * Time and Sales (timesales) is typically used for charting purposes. It captures pricing across a time slice at predefined intervals.
 * Tick data is also available through this endpoint. This results in a very large data set for high-volume symbols, so the time slice needs to be much smaller to keep downloads time reasonable.
 */
@Serializable
data class TimeAndSales(val series: Series) {

    @Serializable
    data class Series(val data: List<SeriesData>)

    @Serializable
    data class SeriesData(
        val time: LocalDateTime,
        val timestamp: Long,
        val price: Double,
        val open: Double,
        val high: Double,
        val low: Double,
        val close: Double,
        val volume: Long,
        val vwap: Double
    )
}
