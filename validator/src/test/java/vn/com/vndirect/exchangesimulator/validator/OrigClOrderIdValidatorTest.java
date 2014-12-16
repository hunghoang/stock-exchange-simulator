package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class OrigClOrderIdValidatorTest {

	private OrigClOrderIdValidator validator;

	@Before
	public void setUp() {
		validator = new OrigClOrderIdValidatorImpl();
	}

	@Test
	public void testShouldRejectCancelRequestWithMismatchedClOrderId() {
		try {
			validator.validate("123", "456");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.UNKNOWN_ORDER.code(), e.getCode());
		}
	}

	@Test
	public void testShouldAcceptCancelRequestWithMatchedClOrderId() throws ValidateException {
		validator.validate("123", "123");
	}

}
