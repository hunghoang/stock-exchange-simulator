package vn.com.vndirect.exchangesimulator.matching;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class BestOrderListTest {

	@Test
	public void testSingleOrder() {
		BestOrderQueue q = new BestOrderQueue(NewOrderSingle.BUY, new PriceRange(16000, 16500, 100), "VND");
		NewOrderSingle order = OrderFactory.createLOBuy(1000, 16200);
		q.add(order);
		Assert.assertEquals(order, q.getOrderList(16200).get(0));
	}
	
	@Test
	public void trackingBestBuy(){
		BestOrderQueue q = new BestOrderQueue(NewOrderSingle.BUY, new PriceRange(16000, 16500, 100), "VND");
		q.add(OrderFactory.createLOBuy(1000, 16200));
		q.add(OrderFactory.createLOBuy(1000, 16500));
		q.add(OrderFactory.createLOBuy(1000, 16400));
		Assert.assertEquals(16500, q.getBestPrice());
	}
	
	@Test
	public void orderBestOrders(){
		BestOrderQueue q = new BestOrderQueue(NewOrderSingle.BUY, new PriceRange(16000, 16500, 100), "VND");
		q.add(OrderFactory.createLOBuy(1000, 16200));
		q.add(OrderFactory.createLOBuy(1000, 16500));
		q.add(OrderFactory.createLOBuy(1000, 16400));
		NewOrderSingle target = OrderFactory.createLOBuy(1000, 16400);
		q.add(target);
		Assert.assertEquals(target, q.getBestOrderList().get(2));
	}

}
