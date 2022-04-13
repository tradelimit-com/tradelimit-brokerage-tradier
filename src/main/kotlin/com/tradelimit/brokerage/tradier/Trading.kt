package com.tradelimit.brokerage.tradier

import com.tradelimit.brokerage.tradier.trading.EquityOrder
import com.tradelimit.brokerage.tradier.trading.OptionOrder

import io.ktor.client.request.*
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
class Trading(private val tradier: TradierClient) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Tradier profile response entity.
     */
    @Serializable
    data class OrderResponse(val order: Order) {
        @Serializable data class Order(
            @SerialName("id") val id: Long,
            @SerialName("status") val status: String,
            @SerialName("partner_id") val partnerId: String)
    }

    suspend fun equityOrder(equityOrder: EquityOrder): OrderResponse {
        log.debug("Making equity order with account id ${equityOrder.accountId} and symbol ${equityOrder.symbol}")
        return tradier.client.post("${tradier.apiUrl}/accounts/{account_id}/orders") {
            contentType(ContentType.parse("application/x-www-form-urlencoded'"))
            body = equityOrder
        }
    }


    suspend fun optionOrder(optionOrder: OptionOrder): OrderResponse {
        log.debug("Making equity order with account id ${optionOrder.accountId} and symbol ${optionOrder.symbol}")
        return tradier.client.post("${tradier.apiUrl}/accounts/{account_id}/orders") {
            contentType(ContentType.parse("application/json"))
//            contentType(ContentType.parse("application/x-www-form-urlencoded'"))
            body = optionOrder
        }
    }
}
