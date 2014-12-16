package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface OrigClOrderIdValidator {
	void validate(String origClOrderId, String clOrderId) throws ValidateException;
}
