package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class SideValidatorTest {

	private SideValidator validator;
	
	@Before
	public void setUp() {
		validator = new SideValidatorImpl();
	}
	
	@Test
	public void testShouldAcceptSideAsBuy() throws ValidateException {
		validator.validate('1');
	}
	
	@Test
	public void testShouldAcceptSideAsSell() throws ValidateException {
		validator.validate('2');
	}
	
	@Test
	public void testShouldRejectSideOtherThanBuyOrSell() {
		try {
			validator.validate('3');
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SIDE.code(), e.getCode());
		}
	}

}
