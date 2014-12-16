package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface OrderReplaceRequestValidator {
	void validate(NewOrderSingle origOrder, OrderReplaceRequest replaceRequest) throws ValidateException;
}
