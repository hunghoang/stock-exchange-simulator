package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-test.xml" })
public class OrderCancelRequestValidatorTest {

	@Autowired
	private OrderCancelRequestValidatorImpl validator;

	private SessionValidator sessionValidator;

	@Test
	public void testInit() {
		assertNotNull(validator);
	}

	@Test
	public void testShouldRejectCancelRequestWithMismatchedSymbol() {
		NewOrderSingle origOrder = new NewOrderSingle();
		origOrder.setSymbol("AAA");
		origOrder.setClOrdID("123");
		OrderCancelRequest request = new OrderCancelRequest();
		request.setSymbol("AAB");
		request.setOrigClOrdID("123");
		
		try {
			validator.validate(origOrder, request);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.ORIGINAL_ORDER_SYMBOL_MISMATCHED.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectCancelRequestWithMismatchedClOrdId() throws ValidateException {
		NewOrderSingle origOrder = new NewOrderSingle();
		origOrder.setSymbol("AAA");
		origOrder.setClOrdID("123");
		OrderCancelRequest request = new OrderCancelRequest();
		request.setSymbol("AAA");
		request.setOrigClOrdID("abc");
		
		sessionValidator = Mockito.mock(SessionValidator.class);
		Mockito.doNothing().when(sessionValidator).validate(request);
		validator.setSessionValidator(sessionValidator);

		try {
			validator.validate(origOrder, request);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.UNKNOWN_ORDER.code(), e.getCode());
		}
	}

	@Test
	public void testShouldAcceptValidCancelRequest() throws ValidateException {
		NewOrderSingle origOrder = new NewOrderSingle();
		origOrder.setSymbol("AAA");
		origOrder.setClOrdID("123");
		OrderCancelRequest request = new OrderCancelRequest();
		request.setSymbol("AAA");
		request.setOrigClOrdID("123");

		sessionValidator = Mockito.mock(SessionValidator.class);
		Mockito.doNothing().when(sessionValidator).validate(request);
		validator.setSessionValidator(sessionValidator);

		validator.validate(origOrder, request);
	}

}
