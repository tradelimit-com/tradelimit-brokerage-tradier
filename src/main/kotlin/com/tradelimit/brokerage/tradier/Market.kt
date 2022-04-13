package com.tradelimit.brokerage.tradier

import com.tradelimit.brokerage.tradier.market.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable


class Market(private val tradier: TradierClient) {

    /**
     * Tradier wraps their response in two separate objects so this actually represents a
     * tradier repsone.
     */
    @Serializable
    data class QuotesResponse(val quotes: Quotes) {
        @Serializable
        data class Quotes(val quote: List<Quote>)
    }

    suspend fun quotes(symbols: List<String>): QuotesResponse.Quotes {
        val symbols = symbols.joinToString(",")
        return tradier.client.get<QuotesResponse>("${tradier.apiUrl}/markets/quotes?symbols=${symbols}&greeks=false").quotes
    }

    /**
     * Get an option chain for a single symbol
     * @param symbol: the ticker symbol to lookup
     * @param expiration: The option expiration
     * @param greeks: should greeks be fetched.
     *
     */
    suspend fun optionChain(symbol: String, expiration: LocalDate, greeks: Boolean = false): OptionChains {
        return tradier.client.get("${tradier.apiUrl}/markets/options/chains?symbol=${symbol}&expiration=$expiration&greeks=$greeks")
    }

    /**
     * Get an options strike prices for a specified expiration date.
     *
     * /v1/markets/options/strikes
     */
    suspend fun optionStrikes(symbol: String, expiration: LocalDate): OptionStrikes {
        return tradier.client.get("${tradier.apiUrl}/markets/options/strikes?symbol=${symbol}&&expiration=${expiration}")
    }


    /**
     * Get expiration dates for a particular underlying.
     * Note that some underlying securities use a different symbol for their weekly options (RUT/RUTW, SPX/SPXW).
     * To make sure you see all expirations, make sure to send the includeAllRoots parameter. This will also ensure
     * any unique options due to corporate actions (AAPL1) are returned.
     *
     * /v1/markets/options/expirations
     */
    suspend fun optionExpiration(symbol: String, includeAllRoots: Boolean = false, strikes: Boolean = false): OptionExpiration {
        return tradier.client.get("${tradier.apiUrl}/markets/options/expirations?symbol=${symbol}&includeAllRoots=${includeAllRoots}&strikes=${strikes}")
    }


    /**
     * Get all options symbols for the given underlying. This will include additional option roots (ex. SPXW, RUTW) if applicable.
     *
     * /v1/markets/options/lookup
     */
    suspend fun optionSymbol(symbol: String): OptionExpiration {
        return tradier.client.get("${tradier.apiUrl}/markets/options/lookup?underlying=${symbol}")
    }

    /**
     * Get historical pricing for a security. This data will usually cover the entire lifetime of the company if sending reasonable start/end times.
     * You can fetch historical pricing for options by passing the OCC option symbol (ex. AAPL220617C00270000) as the symbol.
     */
    suspend fun historicalPricing(symbol: String, interval: Interval, start: LocalDate, end: LocalDate): Historical {
        return tradier.client.get("${tradier.apiUrl}/markets/history?symbol=${symbol}&interval=${interval.name}&start=${start}&end=${end}")
    }


    /**
     * Time and Sales (timesales) is typically used for charting purposes. It captures pricing across a time slice at predefined intervals.
     * Tick data is also available through this endpoint. This results in a very large data set for high-volume symbols, so the time slice needs to be much smaller to keep downloads time reasonable.
     *
     *
     * <pre>
     *  Interval	Data Available (Open)	Data Available (All)
     *  tick	    5 days	                N/A
     *  1min	    20 days	                10 days
     *  5min	    40 days	                18 days
     *  15min	    40 days	                18 days
     * </pre>
     */
    suspend fun timeAndSales(symbol: String, interval: Interval, start: LocalDate, end: LocalDate, sessionFilter: String = "all"): OptionExpiration {
        return tradier.client.get("${tradier.apiUrl}/markets/timesales?symbol=${symbol}&interval=${interval.name}&start=${start}&end=${end}&session_filter=$sessionFilter")
    }


    /**
     * The ETB list contains securities that are able to be sold short with a Tradier Brokerage account.
     * The list is quite comprehensive and can result in a long download response time.
     *
     * /v1/markets/etb
     */
    suspend fun availableShorts(): Securities {
        return tradier.client.get("${tradier.apiUrl}/markets/etb")
    }

    /**
     * Get the intraday market status. This call will change and return information pertaining to the current day.
     * If programming logic on whether the market is open/closed – this API call should be used to determine the current state.
     *
     * /v1/markets/clock
     */
    suspend fun intradayStatus(): IntradayStatus {
        return tradier.client.get("${tradier.apiUrl}/markets/clock?delayed=true")
    }

    /**
     * Get the market calendar for the current or given month. This can be used to plan ahead regarding strategies.
     * However, the Get Intraday Status should be used to determine the current status of the market.
     *
     * /v1/markets/calendar
     */
    suspend fun calendar(month: String, year: String): IntradayStatus {
        return tradier.client.get("${tradier.apiUrl}/markets/calendar?month=${month}&year=${year}\"")
    }


}
