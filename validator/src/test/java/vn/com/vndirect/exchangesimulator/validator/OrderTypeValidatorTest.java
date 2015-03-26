package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class OrderTypeValidatorTest {

	private OrderTypeValidator validator;
	
	@Before
	public void setUp() {
		validator = new OrderTypeValidatorImpl();
	}
	
	@Test
	public void testShouldRejectIfOrderTypeNotLOorATC() {
		try {
			validator.validate('1');
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_ORDER_TYPE.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectIfOrderTypeIsEmpty() {
		try {
			validator.validate(' ');
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_ORDER_TYPE.code(), e.getCode());
		}
	}
	
	@Test
	public void testOrderTypeIsATC() throws ValidateException {
		validator.validate('5');
	}
	
	@Test
	public void testOrderTypeIsLO() throws ValidateException {
		validator.validate('2');
	}

	@Test
	public void testOrderTypeIsMTL() throws ValidateException {
		validator.validate('T');
	}
	@Test
	public void testOrderTypeIsMOK() throws ValidateException {
		validator.validate('K');
	}
	@Test
	public void testOrderTypeIsMAK() throws ValidateException {
		validator.validate('A');
	}
}
