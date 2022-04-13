package com.tradelimit.brokerage.tradier.market

import kotlinx.serialization.Serializable

@Serializable
data class OptionStrikes(val strikes: Strike ) {

    @Serializable
    data class Strike(val strike: List<Double>)
}
