package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface OrderCancelRequestValidator {
	void validate(NewOrderSingle orgOrder, OrderCancelRequest cancelOrder) throws ValidateException;
}
