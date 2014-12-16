package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("OrderCancelRequestValidator")
public class OrderCancelRequestValidatorImpl implements OrderCancelRequestValidator {

	@Autowired
	private OrigSymbolValidator origSymbolValidator;

	@Autowired
	private SessionValidator sessionValidator;

	@Autowired
	private OrigClOrderIdValidator origClOrderIdValidator;

	@Override
	public void validate(NewOrderSingle orgOrder, OrderCancelRequest cancelOrder) throws ValidateException {
		origSymbolValidator.validate(orgOrder.getSymbol(), cancelOrder.getSymbol());
		sessionValidator.validate(cancelOrder);
		origClOrderIdValidator.validate(orgOrder.getClOrdID(), cancelOrder.getOrigClOrdID());
	}

	protected void setSessionValidator(SessionValidator sessionValidator) {
		this.sessionValidator = sessionValidator;
	}

}
