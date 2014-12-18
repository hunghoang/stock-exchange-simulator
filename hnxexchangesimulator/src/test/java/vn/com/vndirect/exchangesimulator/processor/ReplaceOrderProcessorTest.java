package vn.com.vndirect.exchangesimulator.processor;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

public class ReplaceOrderProcessorTest {

	@Test
	public void testGenerateNewOrderWhenModifyPrice() {
		ReplaceOrderProcessor processor = new ReplaceOrderProcessor(null, null);
		
		OrderReplaceRequest orderReplaceRequest = new OrderReplaceRequest();
		orderReplaceRequest.setPrice(20000);
		orderReplaceRequest.setSymbol("VND");
		NewOrderSingle  originNewOrderSingle = new NewOrderSingle();
		originNewOrderSingle.setOrderQty(1000);
		originNewOrderSingle.setSide('1');
		originNewOrderSingle.setOrdType('2');
		NewOrderSingle  newOrderSingle = processor.generateNewOrder(orderReplaceRequest, originNewOrderSingle);
		
		Assert.assertEquals(20000, newOrderSingle.getPrice(), 0.1);
		Assert.assertEquals(1000, newOrderSingle.getOrderQty());
		
	}
	
	@Test
	public void testGenerateNewOrderWhenModifyQuantity() {
		ReplaceOrderProcessor processor = new ReplaceOrderProcessor(null, null);
		
		OrderReplaceRequest orderReplaceRequest = new OrderReplaceRequest();
		orderReplaceRequest.setPrice(20000);
		orderReplaceRequest.setSymbol("VND");
		orderReplaceRequest.setOrderQty(4000);
		orderReplaceRequest.setCashOrderQty(1000);
		NewOrderSingle  originNewOrderSingle = new NewOrderSingle();
		originNewOrderSingle.setOrderQty(10000);
		originNewOrderSingle.setSide('1');
		originNewOrderSingle.setOrdType('2');
		NewOrderSingle  newOrderSingle = processor.generateNewOrder(orderReplaceRequest, originNewOrderSingle);
		
		Assert.assertEquals(20000, newOrderSingle.getPrice(), 0.1);
		Assert.assertEquals(7000, newOrderSingle.getOrderQty());
	}
	
}
