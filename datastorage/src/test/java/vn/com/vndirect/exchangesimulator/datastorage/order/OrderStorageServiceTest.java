package vn.com.vndirect.exchangesimulator.datastorage.order;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderCross;

public class OrderStorageServiceTest {

	@Test
	public void testAddCrossOrder() {
		OrderStorageService storageService = new OrderStorageService();
		NewOrderCross order = new NewOrderCross();
		order.setCrossID("orderId");
		storageService.addCrossOrder(order);
		Assert.assertEquals(order, storageService.getOrderCross("orderId"));
	}
	
	@Test
	public void testRemoveCrossOrder() {
		OrderStorageService storageService = new OrderStorageService();
		NewOrderCross order = new NewOrderCross();
		order.setCrossID("orderId");
		storageService.addCrossOrder(order);
		storageService.removeCrossOrder(order);
		Assert.assertNull(storageService.getOrderCross("orderId"));
	}
}
