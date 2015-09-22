package vn.com.vndirect.exchangesimulator.datastorage.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

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
		int totalOrders = 0;
		int numberOfSymbol = 200;
		for(int i = 0; i < numberOrders; i++) {
			String symbol = "VND" + i % numberOfSymbol;
			NewOrderSingle order = OrderFactory.createNewOrder(symbol, 1000, 12d);
			StorageFactory.getStore(symbol).add(order);
			totalOrders++;
		}
		Assert.assertEquals(totalOrders, new OrderStorageService().getAllOrder().size());
	}
	
	@Test
	public void testResetOrderStorage() {
		String symbol = "VND";
		NewOrderSingle order = OrderFactory.createNewOrder(symbol, 1000, 12d);
		OrderStorage os = StorageFactory.getStore(symbol);
		os.add(order);
		StorageFactory.resetStorage();
		Assert.assertEquals(0, os.size());
	}
}
