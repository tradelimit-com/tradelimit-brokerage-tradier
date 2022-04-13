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
