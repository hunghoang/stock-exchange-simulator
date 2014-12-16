package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.beans.factory.annotation.Autowired;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class OrderReplaceRequestValidatorImpl implements OrderReplaceRequestValidator {

	@Autowired
	private OrigSymbolValidator origSymbolValidator;
	
	@Autowired
	private SessionValidator sessionValidator;
	
	@Autowired
	private OrigClOrderIdValidator origClOrderIdValidator;

	@Autowired
	private PriceValidator priceValidator;
	
	@Autowired
	private QuantityValidator quantityValidator;
	
	@Autowired
	private ReplaceQuantityValidator replaceQuantityValidator;
	
	@Override
	public void validate(NewOrderSingle origOrder, OrderReplaceRequest replaceRequest) throws ValidateException {
		origSymbolValidator.validate(origOrder.getSymbol(), replaceRequest.getSymbol());
		sessionValidator.validate(replaceRequest, origOrder.getOrdType());
		origClOrderIdValidator.validate(origOrder.getClOrdID(), replaceRequest.getOrigClOrdID());
		priceValidator.validate(replaceRequest.getSymbol(), replaceRequest.getPrice());
		quantityValidator.validate(replaceRequest.getCashOrderQty());
		replaceQuantityValidator.validate(origOrder.getOrderQty(), replaceRequest.getCashOrderQty());
	}

}
