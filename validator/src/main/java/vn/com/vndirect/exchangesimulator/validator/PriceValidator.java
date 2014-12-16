package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface PriceValidator {
	void validate(String symbol, double price) throws ValidateException;
}
