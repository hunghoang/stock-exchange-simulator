package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface SessionValidator {
	void validate(NewOrderSingle order) throws ValidateException;
	void validate(OrderCancelRequest request) throws ValidateException;
	void validate(OrderReplaceRequest request, char origOrderType) throws ValidateException; 
}
