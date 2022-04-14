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
