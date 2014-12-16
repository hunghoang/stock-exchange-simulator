package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface QuantityValidator {
	void validate(int quantity) throws ValidateException;
}
