package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class OrigSymbolValidatorTest {

	private OrigSymbolValidator validator;

	@Before
	public void setUp() {
		validator = new OrigSymbolValidatorImpl();
	}

	@Test
	public void testShouldRejectCancelRequestWithMismatchedSymbol() {
		try {
			validator.validate("AAA", "AAB");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.ORIGINAL_ORDER_SYMBOL_MISMATCHED.code(),
					e.getCode());
		}
	}

	@Test
	public void testShouldAcceptCancelRequestWithMatchedSymbol() throws ValidateException {
		validator.validate("AAA", "AAA");
	}

}
