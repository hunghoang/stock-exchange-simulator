package vn.com.vndirect.exchangesimulator.validator;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public interface NewOrderSingleValidator {
	void validate(NewOrderSingle order) throws ValidateException;
	
	public PriceValidator getPriceValidator();

	public QuantityValidator getQuantityValidator();

	public OrderTypeValidator getOrderTypeValidator();

	public SideValidator getSideValidator();
	public AccountValidator getAccountValidator();
	public SessionValidator getSessionValidator();
}
