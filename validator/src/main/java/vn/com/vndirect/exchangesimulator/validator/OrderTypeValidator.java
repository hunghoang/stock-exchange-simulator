package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface OrderTypeValidator {
	void validate(char ordType) throws ValidateException;
}
