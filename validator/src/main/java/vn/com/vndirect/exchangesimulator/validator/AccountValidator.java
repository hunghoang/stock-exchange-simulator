package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface AccountValidator {
	void validate(String account) throws ValidateException;
}
