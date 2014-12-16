package vn.com.vndirect.exchangesimulator.datastorage.order;

import static org.junit.Assert.*;

import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorage;
import vn.com.vndirect.exchangesimulator.datastorage.order.StorageFactory;

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
	
}
