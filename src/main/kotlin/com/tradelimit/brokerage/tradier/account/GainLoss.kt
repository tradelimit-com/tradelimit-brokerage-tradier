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
