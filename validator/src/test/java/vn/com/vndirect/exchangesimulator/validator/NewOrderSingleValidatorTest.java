package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.constant.Side;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-test.xml" })
public class NewOrderSingleValidatorTest {

	@Autowired
	private NewOrderSingleValidatorImpl validator;

	private SessionValidator sessionValidator;

	@Test
	public void test() {
		assertNotNull(validator);
	}
	
	@Ignore
	@Test
	public void testShouldAcceptValidOrder() throws ValidateException {
		NewOrderSingle order = new NewOrderSingle();
		order.setAccount("021C000998");
		order.setOrdType(OrderType.LO.orderType());
		order.setSide(Side.BUY.side());
		order.setSymbol("STP");
		order.setOrderQty(100000);
		order.setPrice(8600d);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		validator.validate(order);
	}

}
