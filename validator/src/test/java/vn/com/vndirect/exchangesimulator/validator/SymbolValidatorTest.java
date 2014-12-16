package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

import vn.com.vndirect.exchangesimulator.constant.SecurityTradingStatus;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.service.SecurityService;
import vn.com.vndirect.exchangesimulator.validator.SymbolValidator;
import vn.com.vndirect.exchangesimulator.validator.SymbolValidatorImpl;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@RunWith(MockitoJUnitRunner.class)
public class SymbolValidatorTest {

	private SymbolValidator validator;
	
	@Mock
	private SecurityService securityService;
	
	@Before
	public void setUp() {
		validator = new SymbolValidatorImpl(securityService);
	}
	
	@Test
	public void testShouldRejectOrderWithEmptySymbol() {
		try {
			validator.validate("");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.EMPTY_FIELD.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectOrderWithNonExistedSymbol() {
		when(securityService.getSecurityBySymbol("XYZ")).thenReturn(null);
		
		try {
			validator.validate("XYZ");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_SYMBOL.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldAcceptOrderWithNewStatus() throws ValidateException {
		SecurityStatus security = new SecurityStatus();
		security.setSecurityTradingStatus(SecurityTradingStatus.NEW.status());
		when(securityService.getSecurityBySymbol("XYZ")).thenReturn(security);
		
		validator.validate("XYZ");
	}
	
	@Test
	public void testShouldRejectOrderWithSuspendedStatus() {
		SecurityStatus security = new SecurityStatus();
		security.setSecurityTradingStatus(SecurityTradingStatus.SUSPENDED.status());
		when(securityService.getSecurityBySymbol("XYZ")).thenReturn(security);
		
		try {
			validator.validate("XYZ");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.SUSPENDED_SYMBOL.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectOrderWithCanceledStatus() {
		SecurityStatus security = new SecurityStatus();
		security.setSecurityTradingStatus(SecurityTradingStatus.CANCELED.status());
		when(securityService.getSecurityBySymbol("XYZ")).thenReturn(security);
		
		try {
			validator.validate("XYZ");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.CANCELED_SYMBOL.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldAcceptOrderWithNormalStatus() throws ValidateException {
		SecurityStatus security = new SecurityStatus();
		security.setSecurityTradingStatus(SecurityTradingStatus.NORMAL.status());
		when(securityService.getSecurityBySymbol("XYZ")).thenReturn(security);
		
		validator.validate("XYZ");
	}
	
	@Test
	public void testShouldRejectOrderWithHaltedStatus() {
		SecurityStatus security = new SecurityStatus();
		security.setSecurityTradingStatus(SecurityTradingStatus.HALTED.status());
		when(securityService.getSecurityBySymbol("XYZ")).thenReturn(security);
		
		try {
			validator.validate("XYZ");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.HALTED_SYMBOL.code(), e.getCode());
		}
	}
	
}
