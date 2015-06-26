package vn.com.vndirect.exchangesimulator.datastorage.order;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorage;
import vn.com.vndirect.exchangesimulator.datastorage.order.StorageFactory;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderFactory;

public class StorageFactoryTest {

	@Test
	public void testGetStoreWithSameSymbol(){
		OrderStorage store1 = (OrderStorage) StorageFactory.getStore("VND");
		OrderStorage store2 = (OrderStorage) StorageFactory.getStore("VND");
		assertNotNull(store1);
		assertNotNull(store2);
		assertEquals(store1, store2);
	}
	
	@Test
	public void testGetTwoStoreWithDifferentSymbol(){
		OrderStorage store1 = (OrderStorage) StorageFactory.getStore("VND");
		OrderStorage store2 = (OrderStorage) StorageFactory.getStore("AAA");
		assertNotNull(store1);
		assertNotNull(store2);
		assertNotEquals(store1, store2);
	}
	
	@Test
	public void testGetAllOrder() {
		int numberOrders = 1000;
		for(int i = 0; i < numberOrders; i++) {
			NewOrderSingle order = OrderFactory.createNewOrder("VND" + i, 1000, 12d);
			StorageFactory.getStore("VND" + i).add(order);
		}
		Assert.assertEquals(numberOrders, new OrderStorageService().getAllOrder().size());
	}
	
}
