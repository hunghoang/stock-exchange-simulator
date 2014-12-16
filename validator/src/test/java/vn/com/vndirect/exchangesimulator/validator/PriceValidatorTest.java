package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.service.SecurityService;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@RunWith(MockitoJUnitRunner.class)
public class PriceValidatorTest {

	private PriceValidator validator;
	
	@Mock
	private SecurityService securityService;
	
	@Before
	public void setUp() {
		validator = new PriceValidatorImpl(securityService);
	}
	
	@Test
	public void testShouldRejectOrderWithPriceLowerThanFloorPrice() {
		SecurityStatus security = new SecurityStatus();
		security.setLowPx(1000d);
		security.setHighPx(1200d);
		when(securityService.getSecurityBySymbol("AAA")).thenReturn(security);
		
		NewOrderSingle order = new NewOrderSingle();
		order.setSymbol("AAA");
		order.setPrice(0d);
		try {
			validator.validate(order.getSymbol(), order.getPrice());
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.PRICE_TOO_LOW.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectOrderWithPriceHigherThanCeilingPrice() {
		SecurityStatus security = new SecurityStatus();
		security.setLowPx(1000d);
		security.setHighPx(1200d);
		when(securityService.getSecurityBySymbol("AAA")).thenReturn(security);
		
		NewOrderSingle order = new NewOrderSingle();
		order.setSymbol("AAA");
		order.setPrice(100000000000000000d);
		try {
			validator.validate(order.getSymbol(), order.getPrice());
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.PRICE_TOO_HIGH.code(), e.getCode());
		}
	}
	
	@Test
	public void testShoudRejectOrderWithPriceNotDivisibleByPriceStep() {
		SecurityStatus security = new SecurityStatus();
		security.setLowPx(1000d);
		security.setHighPx(2000d);
		when(securityService.getSecurityBySymbol("AAA")).thenReturn(security);
		
		NewOrderSingle order = new NewOrderSingle();
		order.setSymbol("AAA");
		order.setPrice(1110d);
		try {
			validator.validate(order.getSymbol(), order.getPrice());
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_PRICE_STEP.code(), e.getCode());
		}
	}
}
