import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class OrderClass(val cls: String) {
    @SerialName("option")
    OPTION("option"),
    @SerialName("equity")
    EQUITY("equity")
}

/**
 * The type of order to be placed. One of: market, limit, stop, stop_limit
 */
@Serializable
enum class OrderType(val type: String) {
    @SerialName("market")
    MARKET("market"),
    @SerialName("limit")
    LIMIT("limit"),
    @SerialName("stop")
    STOP("stop"),
    @SerialName("stop_limit")
    STOP_LIMIT("stop_limit")
}

/**
 * Time the order will remain active. One of: day, gtc, pre, post
 */
@Serializable
enum class OrderDuration(val type: String) {
    @SerialName("day")
    DAY("day"),

    @SerialName("gtc")
    GTC("gtc"),

    @SerialName("pre")
    PRE("pre"),

    @SerialName("post")
    POST("post")
}

/**
 * The side of the order. One of: buy, buy_to_cover, sell, sell_short
 */
@Serializable
enum class OrderSide(val type: String) {
    @SerialName("buy")
    BUY("buy"),
    @SerialName("buy_to_cover")
    BUY_TO_COVER("buy_to_cover"),
    @SerialName("sell")
    SELL("sell"),
    @SerialName("sell_short")
    SELL_SHORT("sell_short")
}

