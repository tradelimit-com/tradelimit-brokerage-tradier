# tradelimit-brokerage-tradier
Tradier client, written Kotlin with the intent of being small and efficient with a simple DSL.




### Equity Orders

```kotlin
val trade = tradier.trading.equityOrder(accountId = "${your_api_token}") {
    symbol = "AMD"
    price = 1.0
    type = OrderType.LIMIT
    side = EquityOrder.Side.BUY
    quantity = 1
    duration = OrderDuration.DAY
}
```


### Option Orders

##### Single Leg Orders
Place an order to trade a single option.
```kotlin
val trade = tradier.trading.optionOrder(accountId = "${your_api_token}")  {
    symbol = "SPY"
    type = OrderType.MARKET
    orderDuration = OrderDuration.DAY
    legs {
        leg {
            side = OptionOrder.Side.BUY_TO_OPEN
            quantity = 1
            optionSymbol = "SPY140118C00195000"
        }
    }
}
```

##### Multileg Option Orders
Place a multileg order with up to 4 legs. This order type allows for simple and complex option strategies.
```kotlin
 val trade = tradier.trading.multiLegOrder(accountId = "${your_api_token}") {
    symbol = "SPY"
    orderDuration = OrderDuration.DAY
    type = OrderType.MARKET
    price = 1.0
    tag = "simple spy iron condor"
    legs {
        leg {
            side = OptionOrder.Side.BUY_TO_OPEN
            quantity = 1
            optionSymbol = "SPY140118C00195000"
        }
        leg {
            side = OptionOrder.Side.SELL_TO_OPEN
            quantity = 1
            optionSymbol = "SPY140118C00195000"
        }
        leg {
            side = OptionOrder.Side.SELL_TO_OPEN
            quantity = 1
            optionSymbol = "SPY140118C00195000"
        }
        leg {
            side = OptionOrder.Side.BUY_TO_OPEN
            quantity = 1
            optionSymbol = "SPY140118C00195000"
        }
    }
}
```


### Combo Orders
Place a combo order. This is a specialized type of order consisting of one equity leg and one option leg. It can 
optionally include a second option leg, for some strategies.

```kotlin
val trade = tradier.trading.comboOrder(accountId = "${your_api_token}") {
    symbol = "SPY"
    type = ComboOrder.Type.MARKET
    duration = OrderDuration.DAY
    legs {
        leg {
            side = OptionOrder.Side.BUY_TO_OPEN
            quantity = 1
            optionSymbol = "SPY140118C00195000"
        }
    }
}
```

### OTO Orders
Place a one-triggers-other order. This order type is composed of two separate orders sent simultaneously. 

In this example the first order is of type option and if successful ti will trigger the equity order to buy 50 shares
of SPY at market value.
```kotlin
val trade = tradier.trading.otoOrder(accountId = "${your_api_token}")  {
    symbol = "SPY"
    duration = OrderDuration.DAY
    order {
        option {
            symbol = "SPY"
            type = OrderType.MARKET
            orderDuration = OrderDuration.DAY
            legs {
                leg {
                    side = OptionOrder.Side.BUY_TO_OPEN
                    quantity = 1
                    optionSymbol = "SPY140118C00195000"
                }
            }
        }
    }
    triggers {
        equity {
            symbol = "SPY"
            type = OrderType.MARKET
            duration = OrderDuration.DAY
            side = EquityOrder.Side.BUY
            quantity = 50
        }
    }
}
```


### OCO Orders
Place a one-cancels-other order. This order type is composed of two separate orders sent simultaneously
```kotlin
val trade = tradier.trading.ocoOrder(accountId = "${your_api_token}") {
    symbol = "SPY"
    duration = OrderDuration.GTC
    order {
        option {
            symbol = "SPY"
            type = OrderType.MARKET
            orderDuration = OrderDuration.GTC
            legs {
                leg {
                    side = OptionOrder.Side.BUY_TO_OPEN
                    quantity = 1
                    optionSymbol = "SPY140118C00195000"
                }
            }
        }
    }
    order {
        equity {
            symbol = "SPY"
            type = OrderType.MARKET
            duration = OrderDuration.GTC
            side = EquityOrder.Side.BUY
            quantity = 50
        }
    }
}
```

### OTOCO Orders

A One-Triggers-a One Cancels the-Other orders involves two orders-a primary order and two secondary orders. The primary order
may be a live order in the marketplace while the secondary orders, held in a separate order file, are not.
If the primary order executes in full, the secondary orders are released to the marketplace as a One-Cancels-the-Other order (OCO).
The execution of either leg of the OCO order triggers an attempt to cancel the unexecuted order. Partial executions
will also trigger an attempt to cancel the other order. An OTOCO order can be made up of stock orders, single-leg option
orders, or a combination of both. It is possible during volatile market conditions that both legs of an OCO could receive
executions. It is also possible that one order receives a delayed execution, resulting in the execution of both orders.

```kotlin
 val trade = tradier.trading.otocoOrder(accountId = "${your_api_token}") {
    symbol = "SPY"
    duration = OrderDuration.GTC
    order {
        option {
            symbol = "SPY"
            type = OrderType.MARKET
            orderDuration = OrderDuration.GTC
            legs {
                leg {
                    side = OptionOrder.Side.BUY_TO_OPEN
                    quantity = 1
                    optionSymbol = "SPY140118C00195000"
                }
            }
        }
    }
    oco {
        option {
            symbol = "SPY"
            type = OrderType.MARKET
            orderDuration = OrderDuration.GTC
            legs {
                leg {
                    side = OptionOrder.Side.BUY_TO_OPEN
                    quantity = 1
                    optionSymbol = "SPY140118C00195000"
                }
            }
        }
        option {
            symbol = "SPY"
            type = OrderType.MARKET
            orderDuration = OrderDuration.GTC
            legs {
                leg {
                    side = OptionOrder.Side.BUY_TO_OPEN
                    quantity = 3
                    optionSymbol = "SPY140118C00195000"
                }
            }
        }
    }
}

```



### Testing 
Test cases are a little in flux right now as I have some that are integration tests using API tokens and the
trading tests are true unit tests. If you want to run the API tests you will need to set your environment variables
Take a look at the TestClientFactory.kt to see what ENV vars you need to set.


