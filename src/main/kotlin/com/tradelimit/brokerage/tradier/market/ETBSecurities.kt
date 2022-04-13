package com.tradelimit.brokerage.tradier.market

import kotlinx.serialization.Serializable

@Serializable
class ETBSecurities(val securities: Securities) {

    @Serializable
    data class Securities(val security: List<Security>)


    @Serializable
    data class Security(val symbol: String, val exhcange: String, val type: String, val description: String)
}
