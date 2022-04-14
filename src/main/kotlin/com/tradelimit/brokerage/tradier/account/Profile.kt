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
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

/**
 * {
 *  "profile": {
 *  "account": [
 *    {
 *      "account_number": "VA000001",
 *      "classification": "individual",
 *      "date_created": "2016-08-01T21:08:55.000Z",
 *      "day_trader": false,
 *      "option_level": 6,
 *      "status": "active",
 *      "type": "margin",
 *      "last_update_date": "2016-08-01T21:08:55.000Z"
 *    },
 *    {
 *      "account_number": "VA000002",
 *      "classification": "traditional_ira",
 *      "date_created": "2016-08-05T17:24:34.000Z",
 *      "day_trader": false,
 *      "option_level": 3,
 *      "status": "active",
 *      "type": "margin",
 *      "last_update_date": "2016-08-05T17:24:34.000Z"
 *    },
 *    {
 *      "account_number": "VA000003",
 *      "classification": "rollover_ira",
 *      "date_created": "2016-08-01T21:08:56.000Z",
 *      "day_trader": false,
 *      "option_level": 2,
 *      "status": "active",
 *      "type": "cash",
 *      "last_update_date": "2016-08-01T21:08:56.000Z"
 *    }
 *  ],
 *  "id": "id-gcostanza",
 *  "name": "George Costanza"
 *  }
 * }
 */
@Serializable data class Profile(val id: String, val name: String, val account: Account) {

    @Serializable data class Account(
        @SerialName("account_number") val accountNumber: String,
        val classification: String,

        @SerialName("date_created")
        @Serializable(with = ZoneDateTimeSerializer::class)
        val dateCreated: ZonedDateTime,

        @SerialName("day_trader") val boolean: Boolean = false,
        @SerialName("option_level") val optionLevel: Int,
        val status: String,
        val type: String,

        @SerialName("last_update_date")
        @Serializable(with = ZoneDateTimeSerializer::class)
        val lastUpdateDate: ZonedDateTime
    )
}
