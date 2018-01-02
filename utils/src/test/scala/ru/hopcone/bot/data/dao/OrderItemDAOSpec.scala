package ru.hopcone.bot.data.dao


import ru.hopcone.bot.models.Tables._
import ru.hopcone.bot.support.BaseSpec


class OrderItemDAOSpec extends BaseSpec {

  describe("OrderItemDAO") {
    def newOrder = ctx.updateOrder(OrderItemDAO.ensureOrder)

    def newOrderItem = ProductsDAO.randomShopItem

    describe("updates order row data") {
      it("with new order row item") {
        newOrder.orderId shouldNot be(OrderItemDAO.NewEntryId)
      }
    }

    describe(".changeAmount") {
      lazy val orderItemAmount = 5

      def checkOrderItem(order: OrderDataRow, itemId: Int): OrderItemRow = {
        val item = OrderItemDAO.item(order, itemId)
        item shouldNot be(None)
        item.get
      }

      describe("updates/creates row in order") {
        it("with not existing row") {
          val order = newOrder
          val orderItem = newOrderItem

          OrderItemDAO.changeAmount(order, orderItem, orderItemAmount)
          val found = checkOrderItem(order, orderItem.id)
          found.amount should be(orderItemAmount)
        }

        it("with existing row") {
          val order = newOrder
          val orderItem = newOrderItem

          OrderItemDAO.changeAmount(order, orderItem, orderItemAmount)
          OrderItemDAO.changeAmount(order, orderItem, orderItemAmount)
          val found = checkOrderItem(order, orderItem.id)
          found.amount should be(orderItemAmount * 2)
        }

        it("removing shop item by amount") {
          val order = newOrder
          val orderItem = newOrderItem

          OrderItemDAO.changeAmount(order, orderItem, orderItemAmount)
          OrderItemDAO.changeAmount(order, orderItem, -orderItemAmount)

          OrderItemDAO.item(order, orderItem.id) should be(None)
        }
      }
    }
  }
}
