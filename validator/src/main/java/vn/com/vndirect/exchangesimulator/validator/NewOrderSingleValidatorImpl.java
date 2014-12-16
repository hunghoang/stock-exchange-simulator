package vn.com.vndirect.exchangesimulator.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("NewOrderSingleValidator")
public class NewOrderSingleValidatorImpl implements NewOrderSingleValidator {

	@Autowired
	private SymbolValidator symbolValidator;
	
	@Autowired
	private SessionValidator sessionValidator;
	
	@Autowired
	private PriceValidator priceValidator;
	
	@Autowired
	private QuantityValidator quantityValidator;
	
	@Autowired
	private OrderTypeValidator orderTypeValidator;
	
	@Autowired
	private SideValidator sideValidator;
	
	@Autowired
	private AccountValidator accountValidator;
	
	@Override
	public void validate(NewOrderSingle order) throws ValidateException {
		String symbol = order.getSymbol();
		double price = order.getPrice();
		int quantity = order.getOrderQty();
		char ordType = order.getOrdType();
		char side = order.getSide();
		String account = order.getAccount();
		
		accountValidator.validate(account);
		orderTypeValidator.validate(ordType);
		sideValidator.validate(side);
		symbolValidator.validate(symbol);
		quantityValidator.validate(quantity);
		if(OrderType.ATC.orderType() != ordType) {
			priceValidator.validate(symbol, price);
		}
		sessionValidator.validate(order);
	}

	protected void setSessionValidator(SessionValidator sessionValidator) {
		this.sessionValidator = sessionValidator;
	}

}
