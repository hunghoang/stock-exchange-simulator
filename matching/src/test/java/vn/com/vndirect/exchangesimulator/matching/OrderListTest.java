package vn.com.vndirect.exchangesimulator.matching;


import org.junit.Assert;
import org.junit.Test;

public class OrderListTest {

	@Test
	public void testClearEmptyOrderListShouldReduceSize() {
		OrderList orderList = new OrderList();
		orderList.add(OrderFactory.createLOBuy(0, 30000));
		orderList.add(OrderFactory.createLOSell(2000, 30000));
		orderList.clearEmptyOrder();
		Assert.assertEquals(1, orderList.size());
	}

	@Test
	public void testClearEmptyOrderListWithOneOrderShouldReduceSize() {
		OrderList orderList = new OrderList();
		orderList.add(OrderFactory.createLOBuy(0, 30000));
		orderList.clearEmptyOrder();
		Assert.assertEquals(0, orderList.size());
	}

	@Test
	public void testClearEmptyOrderListShouldRemoveEmptyOrder() {
		OrderList orderList = new OrderList();
		orderList.add(OrderFactory.createLOBuy(0, 30000));
		orderList.add(OrderFactory.createLOSell(2000, 30000));
		orderList.clearEmptyOrder();
		Assert.assertEquals(2000, orderList.get(0).getOrderQty());
	}
}
