package com.tradelimit.brokerage.tradier.account

import com.tradelimit.brokerage.tradier.serialization.ZoneDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable data class Positions(val position: List<Position>)

@Serializable data class Position(
    /**
     * Cost of the position
     */
    @SerialName("cost_basis") val costBasis : Double,
    /**
     * Date position was acquired (or most recently updated)
     */
    @SerialName("date_acquired")
    @Serializable(with = ZoneDateTimeSerializer::class)
    val dateAcquired : ZonedDateTime,
    /**
     * 	Unique position identifier
     */
    @SerialName("id") val id : String,
    /**
     * 	Number of shares/contracts (positive numbers indicate long positions, negative numbers indicate short positions)
     */
    @SerialName("quantity") val quantity : Double,
    /**
     * Security symbol
     */
    @SerialName("symbol") val symbol : String,
)
