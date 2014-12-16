package vn.com.vndirect.exchangesimulator.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class AccountValidatorImplTest {

	private AccountValidator validator;
	
	@Before
	public void setUp() {
		validator = new AccountValidatorImpl();
	}
	
	@Test
	public void testShouldRejectAccountWithout10characters() {
		try {
			validator.validate("12345");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_ACCOUNT_LENGTH.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectNullAccount() {
		try {
			validator.validate(null);
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.EMPTY_ACCOUNT.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectAccountWithoutLetters() {
		try {
			validator.validate("1234567890");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.ACCOUNT_MUST_CONTAIN_LETTERS_AND_DIGITS.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectAccountWithoutDigits() {
		try {
			validator.validate("abcdefGHIj");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.ACCOUNT_MUST_CONTAIN_LETTERS_AND_DIGITS.code(), e.getCode());
		}
	}
	
	@Test
	public void testShouldRejectAccountWithSpecialCharacters() {
		try {
			validator.validate("123C56!890");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.ACCOUNT_MUST_CONTAIN_LETTERS_AND_DIGITS.code(), e.getCode());
		}
	}
	
	@Test
	public void testValidAccount() {
		try {
			validator.validate("123C56a890");
		} catch (ValidateException e) {
			System.err.println(e.getMessage());
			fail("Must pass");
		}
	}
	
	@Test
	public void testShouldRejectAccountWithNotRightLetterAtFourthPosition() {
		try {
			validator.validate("123A56a890");
			fail("Must throw exception");
		} catch (ValidateException e) {
			assertEquals(ValidateCode.INVALID_ACCOUNT_FORMAT.code(), e.getCode());
		}
	}

}
