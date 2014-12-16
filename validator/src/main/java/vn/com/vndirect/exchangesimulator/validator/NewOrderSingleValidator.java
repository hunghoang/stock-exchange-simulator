package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface NewOrderSingleValidator {
	void validate(NewOrderSingle order) throws ValidateException;
}
