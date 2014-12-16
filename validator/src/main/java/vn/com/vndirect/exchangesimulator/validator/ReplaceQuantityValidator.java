package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface ReplaceQuantityValidator {

	void validate(int origQty, int qty) throws ValidateException;
}