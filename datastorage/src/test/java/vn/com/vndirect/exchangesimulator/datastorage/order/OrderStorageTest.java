package vn.com.vndirect.exchangesimulator.datastorage.order;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderFactory;

public class OrderStorageTest {

	private Storage<NewOrderSingle> store;
	
	@Before public void setup() {
		store = new OrderStorage();
	}
	
	@Test
	public void testShouldAddNewOrder() {
		NewOrderSingle expectedOrder = OrderFactory.createNewOrder("VND", 1000, 12.0);		
		store.add(expectedOrder);
		Assert.assertEquals(expectedOrder, store.get(expectedOrder.getOrderId()));
	}
	
	@Test public void testShouldUpdateOrder() {
		NewOrderSingle order1 = OrderFactory.createNewOrder("VND", 1000, 12.0);
		store.add(order1);
		
		order1.setOrderQty(2000);
		store.add(order1);
		
		Assert.assertEquals(order1, store.get(order1.getOrderId()));
		Assert.assertEquals(2000, store.get(order1.getOrderId()).getOrderQty());
		
	}
	
	@Test public void removeOrderWhenStorageNotContain() {
		NewOrderSingle order = OrderFactory.createNewOrder("VND", 1000, 12d);
		Assert.assertFalse(store.remove(order.getOrderId()));
	}
	
	@Test public void shouldRemoveOrder() {
		NewOrderSingle order = OrderFactory.createNewOrder("VND", 1000, 12d);
		store.add(order);
		Assert.assertTrue(store.remove(order.getOrderId()));
		Assert.assertNull(store.get(order.getOrderId()));
	}
	
	@Test public void shouldGetOrder() {
		NewOrderSingle order1 = OrderFactory.createNewOrder("VND", 1000, 11d);
		NewOrderSingle order2 = OrderFactory.createNewOrder("AAA", 1100, 11d);
		NewOrderSingle order3 = OrderFactory.createNewOrder("ACB", 1100, 11d);
		
		store.add(order1);
		store.add(order2);
		store.add(order3);
		
		Assert.assertNotNull(store.get(order2.getOrderId()));
	}
	
	@Test public void returnNullWhenStorageNotContain() {
		NewOrderSingle order = OrderFactory.createNewOrder("AAA", 1000, 12d);
		Assert.assertNull(store.get(order.getOrderId()));
	}
	
	@Test public void returnEmpty() {
		Assert.assertTrue(store.remain().size() == 0);
	}
	
	@Test
	public void testGetOneRemainOrder(){
		NewOrderSingle order = OrderFactory.createNewOrder("AAA", 1000, 12d);
		store.add(order);
		List<NewOrderSingle> result = store.remain();
		
		assertEquals(1, result.size());
		assertEquals(1000, result.get(0).getOrderQty());
	}
	
	@Test public void testIsEmpty() {
		Assert.assertTrue(store.isEmpty());
	}
	
	@Test public void shouldNotEmpty() {
		NewOrderSingle order = OrderFactory.createNewOrder("VND", 1000, 12d);
		store.add(order);
		Assert.assertFalse(store.isEmpty());
	}
	
	@Test public void sizeIsZero() {
		Assert.assertTrue(0 == store.size());
	}
	
	@Test
	public void sizeOfStorageIsNotZero() {
		NewOrderSingle order = OrderFactory.createNewOrder("VND", 1000, 12d);
		store.add(order);
		Assert.assertTrue(store.size() > 0);
	}
	
}
