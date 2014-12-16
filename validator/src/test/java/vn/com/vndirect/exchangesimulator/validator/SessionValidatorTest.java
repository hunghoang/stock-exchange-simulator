package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.service.SessionService;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@RunWith(MockitoJUnitRunner.class)
public class SessionValidatorTest {

	private SessionValidator validator;
	private NewOrderSingle order;
	private OrderCancelRequest cancelRequest;
	private OrderReplaceRequest replaceRequest;

	@Mock
	private SessionService sessionService;

	@Before
	public void setUp() {
		order = new NewOrderSingle();
		order.setOrdType(OrderType.LO.orderType());
		cancelRequest = new OrderCancelRequest();
		replaceRequest = new OrderReplaceRequest();
		validator = new SessionValidatorImpl(sessionService);
	}

	@Test
	public void testValidatePlaceOrderDuringLOSession() {
		when(sessionService.isLO()).thenReturn(true);
		when(sessionService.isATC1()).thenReturn(false);
		try {
			validator.validate(order);
		} catch (ValidateException e) {
			fail("Must pass validation");
		}
	}

	@Test
	public void testValidatePlaceOrderDuringATC1Session() {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(true);
		try {
			validator.validate(order);
		} catch (ValidateException e) {
			fail("Must pass validation");
		}
	}

	@Test
	public void testValidatePlaceOrderDuringOtherSessions() {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(false);
		try {
			validator.validate(order);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SESSION.code(), e.getCode());
		}
	}

	@Test
	public void testRejectATCOrderInLOSession() {
		when(sessionService.isLO()).thenReturn(true);
		order.setOrdType(OrderType.ATC.orderType());
		try {
			validator.validate(order);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SESSION.code(), e.getCode());
		}
	}
	
	@Test
	public void testInLast5minsATC() throws ValidateException {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(true);

		validator.validate(order);
	}

	@Test
	public void testShouldAcceptCancelRequestInLOSession() throws ValidateException {
		when(sessionService.isLO()).thenReturn(true);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(false);
		
		validator.validate(cancelRequest);
	}
	
	@Test
	public void testShouldAcceptCancelRequestInATCPhase1Session() throws ValidateException {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(true);
		when(sessionService.isATC2()).thenReturn(false);
		
		validator.validate(cancelRequest);
	}
	
	@Test
	public void testShouldRejectCancelRequestInATCPhase2Session() {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(true);
		
		try {
			validator.validate(cancelRequest);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SESSION_CANCEL_ORDER.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectCancelRequestInOtherSession() {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(false);
		
		try {
			validator.validate(cancelRequest);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SESSION_CANCEL_ORDER.code(), e.getCode());
		}
	}

	@Test
	public void testShouldAcceptReplaceRequestInLOSession() throws ValidateException {
		when(sessionService.isLO()).thenReturn(true);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(false);
		
		validator.validate(replaceRequest, OrderType.LO.orderType());
	}
	
	@Test
	public void testShouldAcceptLOReplaceRequestInATCPhase1Session() throws ValidateException {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(true);
		when(sessionService.isATC2()).thenReturn(false);
		
		validator.validate(replaceRequest, OrderType.LO.orderType());
	}

	@Test
	public void testShouldRejectATCReplaceRequestInATCPhase1Session(){
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(true);
		when(sessionService.isATC2()).thenReturn(false);
		
		try {
			validator.validate(replaceRequest, OrderType.ATC.orderType());
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.CANCEL_REPLACE_NOT_SUPPORTED.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectLOReplaceRequestInATCPhase2Session() {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(true);
		
		try {
			validator.validate(replaceRequest, OrderType.LO.orderType());
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SESSION_REPLACE_ORDER.code(), e.getCode());
		}
	}

	@Test
	public void testShouldRejectReplaceRequestInOtherSession() {
		when(sessionService.isLO()).thenReturn(false);
		when(sessionService.isATC1()).thenReturn(false);
		when(sessionService.isATC2()).thenReturn(false);
		
		try {
			validator.validate(replaceRequest, OrderType.LO.orderType());
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SESSION_REPLACE_ORDER.code(), e.getCode());
		}
	}
}
