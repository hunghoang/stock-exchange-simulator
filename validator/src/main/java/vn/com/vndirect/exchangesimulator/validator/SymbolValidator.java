package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface SymbolValidator {
	void validate(String symbol) throws ValidateException;
}
