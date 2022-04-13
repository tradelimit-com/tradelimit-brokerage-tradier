package com.tradelimit.brokerage.tradier.trading

import OrderClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OptionOrder(
    /**
     * Account id
     */
    @SerialName("account_id") val accountId: String,

    /**
     * equity symbol
     */
    @SerialName("symbol") val symbol: String,

    /**
     *
     * option_symbol	Form	String	Required	SPY140118C00195000
     */
    @SerialName("option_symbol") val optionSymbol: String,

    /**
     * The side of the order. One of: buy, buy_to_cover, sell, sell_short
     */
    @SerialName("side") val side: String,

    @SerialName("quantity") val quantity: String,
    /**
     * The type of order to be placed. One of: market, limit, stop, stop_limit
     */
    @SerialName("type") val type: String,
    /**
     * Time the order will remain active. One of: day, gtc, pre, post
     */
    @SerialName("duration") val duration: String,
    @SerialName("price") val price: String? = null,

    @SerialName("stop") val stop: String? = null,
    /**
     * Order tag.
     * Maximum lenght of 255 characters.
     * Valid characters are letters, numbers and -
     */
    @SerialName("tag") val tag: String? = null


) {

    /**
     * The kind of order to be placed.
     */
    @SerialName("class") val cls: String = OrderClass.OPTION.cls

    private constructor(builder: Builder) : this(
        checkNotNull(builder.accountId) { "Missing cls (class) parameter for order" },
        checkNotNull(builder.symbol) { "Missing symbol parameter for order" },
        checkNotNull(builder.optionSymbol) { "Missing option symbol parameter for order" },
        checkNotNull(builder.type) { "Missing type parameter for order" },
        checkNotNull(builder.quantity) { "Missing symbol parameter for order" },
        checkNotNull(builder.side) { "Missing side parameter for order" },
        checkNotNull(builder.duration) { "Missing duration parameter for order" },
        builder.price,
        builder.stop,
        builder.tag
    )

    companion object {
        inline fun oprionOerder(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var accountId: String? = null
        var symbol: String? = null
        var optionSymbol: String? = null
        var side: String? = null
        var quantity: String? = null
        var type: String? = null
        var duration: String? = null
        var price: String? = null
        var stop: String? = null
        var tag: String? = null
        fun build() = OptionOrder(this)
    }
}
