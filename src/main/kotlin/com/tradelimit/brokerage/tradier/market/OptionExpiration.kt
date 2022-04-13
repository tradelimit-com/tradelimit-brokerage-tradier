package com.tradelimit.brokerage.tradier.market

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class OptionExpiration(val expirations: Expirations) {

    @Serializable
    data class Expirations(val date: List<LocalDate>)
}
