package com.tradelimit.brokerage.tradier.market

import kotlinx.serialization.Serializable

@Serializable
class Securities(val security: List<Security>) {

    @Serializable
    data class Security(val symbol: String,
                        val exchange: String,
                        val type: String,
                        val description: String)

}
