package org.jimmyoak.discussions.tovoornottovo.withoutvos

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

data class OrderId(val value: String)

data class Amount(val value: Double) {
    operator fun plus(adding: Amount): Amount = Amount(value + adding.value)
}

enum class Currency {
    EUR
}

data class Money(val amount: Amount, val currency: Currency) {
    operator fun plus(adding: Money): Money = Money(amount + adding.amount, currency)
}

data class OrderLineDescription(val value: String)

data class OrderLine(val description: OrderLineDescription, val money: Money)

data class Order(val id: OrderId, val lines: List<OrderLine>) {
    fun total(): Money = lines
            .map { it.money }
            .reduce { accumulator, current -> accumulator + current }
}

object Expenses {
    fun totalOf(orders: List<Order>): Money = orders
            .map { it.total() }
            .reduce { accumulator, current -> accumulator + current }
}

class DiscussionTest {
    @Test
    fun `should get total of lines in order`() {
        val order = Order(OrderId("1"), listOf(
                OrderLine(OrderLineDescription("PS4"), Money(Amount(400.0), Currency.EUR)),
                OrderLine(OrderLineDescription("Resident Evil 7"), Money(Amount(35.0), Currency.EUR))
        ))

        assertThat(order.total()).isEqualTo(Money(Amount(435.0), Currency.EUR))
    }

    @Test
    fun `should get total of orders`() {
        val orders = listOf(
                Order(OrderId("1"), listOf(
                        OrderLine(OrderLineDescription("PS4"), Money(Amount(400.0), Currency.EUR)),
                        OrderLine(OrderLineDescription("Resident Evil 7"), Money(Amount(35.0), Currency.EUR))
                )),
                Order(OrderId("2"), listOf(
                        OrderLine(OrderLineDescription("Metal Gear Solid 5"), Money(Amount(30.0), Currency.EUR))
                ))
        )

        assertThat(Expenses.totalOf(orders)).isEqualTo(Money(Amount(465.0), Currency.EUR))
    }
}