package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface OrigSymbolValidator {
	void validate(String origSymbol, String symbol) throws ValidateException;
}
