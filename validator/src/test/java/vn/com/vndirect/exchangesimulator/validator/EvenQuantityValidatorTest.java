package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class EvenQuantityValidatorTest {
	
	private QuantityValidator validator;
	
	@Before
	public void setUp() {
		validator = new EvenQuantityValidatorImpl();
	}
	
	@Test
	public void testShouldRejectOrderWithQtyEqualTo0() {
		try {
			validator.validate(0);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.NON_POSITIVE_QUANTITY.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectOrderWithNegativeQty() {
		try {
			validator.validate(-10);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.NON_POSITIVE_QUANTITY.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectOrderWithQtyNotDivisibleBy100() {
		try {
			validator.validate(150);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_LOT.code(), e.getCode());
		}
	}
	
	@Test
	public void testRejectOrderWithLOTSmallerThan100() {
		try {
			validator.validate(86);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_EVEN_LOT.code(), e.getCode());
		}
	}
}
