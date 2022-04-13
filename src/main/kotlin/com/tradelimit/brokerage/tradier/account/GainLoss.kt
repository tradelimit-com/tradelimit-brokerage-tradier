package com.tradelimit.brokerage.tradier.account

import com.tradelimit.brokerage.tradier.serialization.ZoneDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable data class GainLoss(@SerialName("closed_position") val closedPosition: List<ClosedPosition>) {


    @Serializable data class ClosedPosition(
        /**
         * Date the position was closed
         */
        @Serializable(with = ZoneDateTimeSerializer::class)
        @SerialName("close_date") val closeDate: ZonedDateTime,
        /**
         * Total cost of the position
         */
        @SerialName("cost") val cost: Double,
        /**
         * Gain or loss on the position
         */
        @SerialName("gain_loss") val gainLoss: Double,
        /**
         * Gain or loss represented as percent
         */
        @SerialName("gain_loss_percent") val gainLossPercent: Double,
        /**
         * Date the position was opened
         */
        @Serializable(with = ZoneDateTimeSerializer::class)
        @SerialName("open_date") val openDate: ZonedDateTime,
        /**
         * Total amount received for the position
         */
        @SerialName("proceeds") val proceeds: Double,
        /**
         * Quantity of shares/contracts
         */
        @SerialName("quantity") val quantity: Double,
        /**
         * Symbol of the security held
         */
        @SerialName("symbol") val symbol: String,
        /**
         * Term in months position was held
         */
        @SerialName("term") val term: Int
    )
}
