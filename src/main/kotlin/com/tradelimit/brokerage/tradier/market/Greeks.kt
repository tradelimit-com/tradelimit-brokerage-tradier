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


import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Greeks(
    val delta: Double,
    val gamma: Double,
    val theta: Double,
    val vega: Double,
    val rho: Double,
    val phi: Double,
    @SerialName("bid_iv") val bidIv: Double,
    @SerialName("mid_iv") val midIv: Double,
    @SerialName("ask_iv") val askIv: Double,
    @SerialName("smv_vol") val smvVol: Double,

    @SerialName("updated_at") val updateAt: LocalDateTime
) {
}
