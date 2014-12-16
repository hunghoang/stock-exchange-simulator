package vn.com.vndirect.exchangesimulator;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

public class TestOrderReplaceRequest {

	@Test
	public void testOrderReplaceRequest() {
		OrderReplaceRequest request = new OrderReplaceRequest();
		Assert.assertEquals(0, request.getOrderQty());
		Assert.assertEquals(0, request.getCashOrderQty());
	}
}
