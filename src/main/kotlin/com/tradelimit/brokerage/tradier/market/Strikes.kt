package com.tradelimit.brokerage.tradier.market

import kotlinx.serialization.Serializable

@Serializable
class Strikes(val strikes: Strike) {

    @Serializable
    data class Strike(val strike: List<Float>)


}
