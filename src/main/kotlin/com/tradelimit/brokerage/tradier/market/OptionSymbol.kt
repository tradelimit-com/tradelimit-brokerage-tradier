package com.tradelimit.brokerage.tradier.market

import kotlinx.serialization.Serializable

@Serializable
data class OptionSymbol(val symbols: List<Symbol>) {

    @Serializable
    data class Symbol(val rootSymbol: String, val options: List<String>)
}
