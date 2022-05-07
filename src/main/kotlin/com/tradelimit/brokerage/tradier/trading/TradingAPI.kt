/*
 * Copyright 2022 tradelimit.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tradelimit.brokerage.tradier.trading


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory

/**
 * <pre>
 *     <div id="documentation" class="col-9 pl-5">
 *     <h1 id="getting-started-with-trading">Getting Started with Trading</h1>
 *
 *     <p>We tried our best to make the trading API as easy as possible to work with. There are some essential concepts to understand having to do with a trading flow, but the calls themselves are very straightforward.</p>
 *
 *     <h2 id="buying-and-selling-equities">Buying and Selling Equities</h2>
 *
 *     <p>Placing an order through the Trading API is very simple. There’s no FIXML to learn, no custom formats or XML. There are five required parameters:</p>
 *
 *     <ul>
 *          <li><code class="highlighter-rouge">class</code> - The kind of order to be placed. One of: equity, option, multileg, combo.</li>
 *          <li><code class="highlighter-rouge">symbol</code> - The symbol to be ordered.</li>
 *          <li><code class="highlighter-rouge">duration</code> - The time for which the order will be remain in effect (Day or GTC).</li>
 *          <li><code class="highlighter-rouge">side</code> - The side of the order (buy or sell).</li>
 *          <li><code class="highlighter-rouge">quantity</code> - The number of shares ordered.</li>
 *          <li><code class="highlighter-rouge">type</code> - The type of order to be placed (market, limit, etc.)</li>
 *     </ul>
 *
 *     <p>A properly composed order for 100 shares of AAPL looks like this:</p>
 *
 *     <div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>POST /v1/accounts/12345678/orders HTTP/1.1
 *     Host: api.tradier.com
 *
 *
 *     class=equity&amp;symbol=AAPL&amp;duration=day&amp;side=buy&amp;quantity=100&amp;type=market
 *     </code></pre></div></div>
 *
 *     <h2 id="buying-and-selling-options">Buying and Selling Options</h2>
 *
 *     <p>Placing an order for an option is very similar to that of a stock. There is only one additional field for placing a single-leg:</p>
 *
 *     <ul>
 *          <li><code class="highlighter-rouge">option_symbol</code> an OCC symbol (AAPL140118C00195000) for the option to be ordered.</li>
 *     </ul>
 *
 *     <p>A properly composed order for 100 shares of an AAPL option looks like this:</p>
 *
 *     <div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>POST /v1/accounts/12345678/orders HTTP/1.1
 *     Host: api.tradier.com
 *     Accept: \*
 *
 *     class=option&amp;symbol=AAPL&amp;duration=day&amp;side=sell_to_open&amp;quantity=100
 *     &amp;type=market&amp;option_symbol=AAPL140118C00195000
 *     </code></pre></div></div>
 *
 *     <h2 id="placing-multileg-and-combo-orders">Placing Multileg and Combo orders</h2>
 *
 *     <p>Placing multileg and combo orders uses the same framework and parameters as explained above for single leg orders with only slight modification. Legs only have a three specific data points: side, quantity, and symbol. For option legs you should send <code class="highlighter-rouge">option_symbol</code>, in the instance of combo orders, equity legs should have <code class="highlighter-rouge">option_symbol</code> set to <code class="highlighter-rouge">null</code> as the underlying symbol will be used.</p>
 *
 *     <p>In order to send multiple legs, we’ve adopted standard form-parameter notation: side[index], quantity[index], option_symbol[index], where index is the leg number (based at zero).</p>
 *
 *     <ul>
 *          <li><code class="highlighter-rouge">side[index]</code> the side of the leg.</li>
 *          <li><code class="highlighter-rouge">quantity[index]</code> the quantity of shares/contracts for that leg</li>
 *          <li><code class="highlighter-rouge">option_symbol[index]</code> the OCC symbol (AAPL140118C00195000) for the option. Should be null for equity legs.</li>
 *     </ul>
 *
 *     <p>A properly composed multileg order (spread) looks like this:</p>
 *
 *     <div class="highlighter-rouge"><div class="highlight"><pre class="highlight"><code>POST /v1/accounts/12345678/orders HTTP/1.1
 *     Host: api.tradier.com
 *     Accept: \*
 *
 *     class=multileg&amp;symbol=CSCO&amp;duration=day&amp;type=market
 *     &amp;side[0]=buy_to_open&amp;quantity[0]=1&amp;option_symbol[0]=CSCO150117C00035000
 *     &amp;side[1]=sell_to_open&amp;quantity[1]=1&amp;option_symbol[1]=CSCO140118C00008000
 *     </code></pre></div></div>
 *
 *     <h2 id="prepost-market-sessions">Pre/Post Market Sessions</h2>
 *     <p>Pre and Post market session orders are supported, however some restrictions apply.</p>
 *
 *     <p><strong>Pre-market Session</strong>: 7:00AM EST to 9:24AM EST<br>
 *     <strong>Post-market Session</strong>: 4:00PM EST to 19:55PM EST</p>
 *
 *     <p>Some notes about orders placed for these sessions:</p>
 *
 *     <ul>
 *          <li>Only equity orders are permitted.</li>
 *          <li>Order type must be <code>limit</code></li>
 *          <li>Orders placed in the session will expire at the end of the session</li>
 *          <li>You cannot place orders for pre/post session outside the session window</li>
 *     </ul>
 *
 *     <h2 id="previewing-orders">Previewing Orders</h2>
 *     <p>It is in the investor’s best interest to preview an order before actually placing it. A preview call will return issues and warnings associated with the order (if applicable) as well as commission information should the order be executed. Previewing is also the best way to get started using the Trading API.</p>
 *     <p>By sending <code class="highlighter-rouge">preview=true</code> as a parameter to the Create Order call, you can place “pretend” calls to these APIs.</p>
 *     <p><em>Note: Preview orders are not required — but are recommended. There will be circumstances (i.e. algorithmic trading) where it is unreasonable to preview the order first.</em></p>
 *     <h2 id="market-data">Market Data</h2>
 *     <p>Market data is key to placing accurate trades. As a developer, you’re responsible for making market data requests in order to give users enough information before placing a trade.</p>
 *     </div>
 *  </pre>
 */
class TradingAPI(var apiUrl: String, private val httpClient: HttpClient) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Tradier profile response entity.
     */
    @Serializable
    data class OrderResponse(val order: Order) {

        @Serializable
        data class Order(
            @SerialName("id") val id: Long,
            @SerialName("status") val status: String,
            @SerialName("partner_id") val partnerId: String
        )
    }

    /**
     * Place an order to trade an equity security.
     *
     * <pre>
     *  Parameters
     *      Parameter	Type	Param Type	Required	Values/Example	Default
     *      account_id	Path	String	Required	VA000000
     *      Account number
     *      class	Form	String	Required	equity
     *      The kind of order to be placed.
     *      symbol	Form	String	Required	SPY
     *      Security symbol to be traded.
     *      side	Form	String	Required	buy
     *      The side of the order. One of: buy, buy_to_cover, sell, sell_short
     *      quantity	Form	String	Required	10
     *      The number of shares ordered.
     *      type	Form	String	Required	market
     *      The type of order to be placed. One of: market, limit, stop, stop_limit
     *      duration	Form	String	Required	day
     *      Time the order will remain active. One of: day, gtc, pre, post
     *      price	Form	String	Optional	1.00
     *      Limit price. Required only for limit and stop_limit orders.
     *      stop	Form	String	Optional	1.00
     *      Stop price. Required only for stop and stop_limit orders.
     *      tag	Form	String	Optional	my-tag-example-1
     *      Order tag.
     *      Maximum length of 255 characters.
     *      Valid characters are letters, numbers and -
     *  </pre>
     */
    suspend fun equityOrder(accountId: String, equityOrder: EquityOrder): OrderResponse {
        log.debug("Making equity order with account id $accountId and symbol ${equityOrder.symbol}")

        return httpClient.submitForm("${apiUrl}/accounts/$accountId/orders", formParameters = Parameters.build {
            equityOrder.formParams(this)
        }).body()
    }


    /**
     * Place an order to trade a single option.
     */
    suspend fun optionOrder(accountId: String, optionOrder: OptionOrder): OrderResponse {
        log.debug("Making equity order with account id $accountId and symbol ${optionOrder.symbol}")
        check(optionOrder.legs.size == 1) { "Only one leg for option order, might try multiLegOrder" }

        return httpClient.submitForm("${apiUrl}/accounts/$accountId/orders", formParameters = Parameters.build {
            optionOrder.formParams(this)
        }).body()
    }


    /**
     * Place an order to trade a single option.
     */
    suspend fun multiLegOrder(accountId: String, optionOrder: OptionOrder): OrderResponse {
        log.debug("Making equity order with account id $accountId and symbol ${optionOrder.symbol}")

        return httpClient.submitForm("${apiUrl}/accounts/$accountId/orders", formParameters = Parameters.build {
            append("symbol", optionOrder.symbol)
            append("type", "${optionOrder.type}")
            append("duration", optionOrder.duration.toString())
            append("class", "${OrderClass.MULTILEG}")
            append("price", "${optionOrder.price}")
            optionOrder.legs.appendOptionLegs(this)
            optionOrder.tag?.let { append("tag", it) }
        }).body()
    }



    /**
     * Place a combo order. This is a specialized type of order consisting of one equity leg and one option leg. It can
     * optionally include a second option leg, for some strategies.
     */
    suspend fun comboOrder(accountId: String, order: ComboOrder): OrderResponse {
        log.debug("Making equity order with account id $accountId and symbol ${order.symbol}")

        return httpClient.submitForm("${apiUrl}/accounts/$accountId/orders", formParameters = Parameters.build {
            append("class", "${OrderClass.COMBO}")
            append("symbol", order.symbol)
            append("type", "${order.type}")
            append("duration", order.duration.toString())
            order.price?.let { append("price", "$it")  }
            order.optionLegs.appendOptionLegs(this)
            order.tag?.let { append("tag", it) }
        }).body()
    }


    /**
     * Place a one-triggers-other order. This order type is composed of two separate orders sent simultaneously.
     * The property keys of each order are indexed.
     */
    suspend fun otoOrder(accountId: String, order: OtoOrder): OrderResponse {

        return httpClient.submitForm("${apiUrl}/accounts/$accountId/orders", formParameters = Parameters.build {
            append("class", "${OrderClass.OTO}")
            append("duration", order.duration.toString())

            listOf(order.order, order.triggers).forEachIndexed {idx, order ->
                when(order) {
                    is OptionOrder -> {
                        check(order.legs.size == 1) {"In an OTO order there can be only one leg for options"}
                        order.formParams(this, idx)
                    }

                    is EquityOrder -> {
                        order.formParams(this, idx)
                    }
                    else -> throw IllegalStateException("Received unknown oto order type ${order!!::class}")
                }
            }

        }).body()
    }

    /**
     * Place a one-cancels-other order. This order type is composed of two separate orders sent simultaneously.
     * The property keys of each order are indexed.
     * <pre>
     *  Please note these specific validations:
     *      type must be different for both legs.
     *      If both orders are equities, the symbol must be the same.
     *      If both orders are options, the option_symbol must be the same.
     *      If sending duration per leg, both orders must have the same duration.
     * </pre>
     */
    suspend fun ocoOrder(optionOrder: OptionOrder): OrderResponse {
        throw NotImplementedError("Missing implementation")
    }


    /**
     * Place a one-triggers-one-cancels-other order. This order type is composed of three separate orders sent
     * simultaneously. The property keys of each order are indexed.
     * <pre>
     *  Please note these specific validations:
     *      If all equity orders, second and third orders must have the same symbol.
     *      If all option orders, second and third orders must have the same option_symbol.
     *      Second and third orders must always have a different type.
     *      If sending duration per leg, second and third orders must have the same duration.
     * </pre>
     * POST: /v1/accounts/{account_id}/orders
     */
    suspend fun otcoOrder(optionOrder: OptionOrder): OrderResponse {
        throw NotImplementedError("Missing implementation")
    }



    /**
     * Helper to converf
     */
    private fun OptionOrder.formParams(pb: ParametersBuilder, idx: Int? = null ) {
        val leg = legs.single()

        with(pb) {
            append(tagName("symbol", idx), symbol)
            append(tagName("type", idx), "$type")
            append(tagName("duration", idx), duration.toString())
            append(tagName("class", idx), OrderClass.OPTION.toString())
            append(tagName("option_symbol", idx), leg.optionSymbol)
            append(tagName("quantity", idx), "${leg.quantity}")
            append(tagName("side", idx), "${leg.side}")
            tag?.let { append("tag", it) }
        }
    }

    private fun EquityOrder.formParams(pb: ParametersBuilder, idx: Int? = null) {
        with(pb) {
            append("class", cls)
            append(tagName("symbol", idx), symbol)
            append(tagName("side", idx), side.token)
            append(tagName("quantity", idx), "$quantity")
            append(tagName("type", idx), "$type")
            append(tagName("duration", idx), "$duration")
            price?.let { pb.append(tagName("price", idx), it.toString()) }
            stop?.let { append(tagName("stop", idx), it) }
            tag?.let { append(tagName("tag", idx), it) }
        }
    }


    /**
     * Helper function for formatting tag names if index is supplied or not.
     */
    private fun tagName(s: String, idx: Int? = null) = idx?.let {"$s[$idx]"} ?: s


    /**
     * This function will simply append the option legs to the form builder.
     */
    private fun List<OptionLeg>.appendOptionLegs(pb: ParametersBuilder) =  forEachIndexed { idx, order ->
        pb.append("side[$idx]", "${order.side}")
        pb.append("quantity[$idx]", "${order.quantity}")
        pb.append("option_symbol[$idx]", order.optionSymbol)
    }
}
