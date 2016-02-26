package vn.com.vndirect.exchangesimulator.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("NewOrderSingleValidator")
public class NewOrderSingleValidatorImpl implements NewOrderSingleValidator {

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
		char ordType = order.getOrdType();
		char side = order.getSide();
		String account = order.getAccount();
		
		accountValidator.validate(account);
		orderTypeValidator.validate(ordType);
		sideValidator.validate(side);
		if (OrderType.ATC.orderType() != ordType
				&& OrderType.MAK.orderType() != ordType
				&& OrderType.MOK.orderType() != ordType
				&& OrderType.MTL.orderType() != ordType) {
			priceValidator.validate(symbol, price);
		}
	}

	protected void setSessionValidator(SessionValidator sessionValidator) {
		this.sessionValidator = sessionValidator;
	}

	public PriceValidator getPriceValidator() {
		return priceValidator;
	}

	public void setPriceValidator(PriceValidator priceValidator) {
		this.priceValidator = priceValidator;
	}

	public QuantityValidator getQuantityValidator() {
		return quantityValidator;
	}

	public void setQuantityValidator(QuantityValidator quantityValidator) {
		this.quantityValidator = quantityValidator;
	}

	public OrderTypeValidator getOrderTypeValidator() {
		return orderTypeValidator;
	}

	public void setOrderTypeValidator(OrderTypeValidator orderTypeValidator) {
		this.orderTypeValidator = orderTypeValidator;
	}

	public SideValidator getSideValidator() {
		return sideValidator;
	}

	public void setSideValidator(SideValidator sideValidator) {
		this.sideValidator = sideValidator;
	}

	public AccountValidator getAccountValidator() {
		return accountValidator;
	}

	public void setAccountValidator(AccountValidator accountValidator) {
		this.accountValidator = accountValidator;
	}

	public SessionValidator getSessionValidator() {
		return sessionValidator;
	}
}
