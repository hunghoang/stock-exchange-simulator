package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface SideValidator {
	void validate(char side) throws ValidateException;
}
