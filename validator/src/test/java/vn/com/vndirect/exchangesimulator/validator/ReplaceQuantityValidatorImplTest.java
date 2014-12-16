package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class ReplaceQuantityValidatorImplTest {

	private ReplaceQuantityValidator validator;
	
	@Before
	public void setUp() {
		validator = new ReplaceQuantityValidatorImpl();
	}
	
	@Test
	public void testShouldRejectReplaceEvenToOddOrder() {
		try {
			validator.validate(200, 89);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.REPLACE_FROM_EVEN_TO_ODD_NOT_SUPPORTED.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldAcceptReplaceEvenToEvenOrder() throws ValidateException {
		validator.validate(100, 200);
	}
	
	@Test
	public void testShouldRejectOddToEventOrder() {
		try {
			validator.validate(88, 100);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.REPLACE_FROM_ODD_TO_EVEN_NOT_SUPPORTED.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldAcceptOddToOddOrder() throws ValidateException {
		validator.validate(88, 10);
	}
}
