package vn.com.vndirect.exchangesimulator.robotTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.SecurityStatusManager;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.model.TradSesStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;
import vn.com.vndirect.exchangesimulator.validator.AccountValidator;
import vn.com.vndirect.exchangesimulator.validator.OrderTypeValidator;
import vn.com.vndirect.exchangesimulator.validator.PriceValidator;
import vn.com.vndirect.exchangesimulator.validator.QuantityValidator;
import vn.com.vndirect.exchangesimulator.validator.SessionValidator;
import vn.com.vndirect.exchangesimulator.validator.SideValidator;
import vn.com.vndirect.exchangesimulator.validator.SymbolValidator;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class PlaceOrderRobotTest {

	private SymbolValidator symbolValidator;

	private SessionValidator sessionValidator;

	private PriceValidator priceValidator;

	private QuantityValidator quantityValidator;

	private OrderTypeValidator orderTypeValidator;

	private SideValidator sideValidator;

	private AccountValidator accountValidator;
	
	private String messagePlaceOrder = "0";

	private Map<String, TradSesStatus> sessionCode = new HashMap<String, TradSesStatus>();
	
	public PlaceOrderRobotTest() {
		sessionCode.put("CONT", TradSesStatus.LO);
		sessionCode.put("PREOPEN", TradSesStatus.PREOPEN);
		sessionCode.put("INTERMISSION", TradSesStatus.INTERMISSION);
		sessionCode.put("ATC1", TradSesStatus.ATC1);
		sessionCode.put("ATC2", TradSesStatus.ATC2);
		sessionCode.put("PTCLOSE", TradSesStatus.PTCLOSE);
		sessionCode.put("ENDOFDAY", TradSesStatus.ENDOFDAY);
		
		ApplicationContext springContext = RobotFrameworkTest.getApplicationContext();
		symbolValidator = (SymbolValidator) springContext.getBean("SymbolValidator");
		sessionValidator = (SessionValidator) springContext.getBean("SessionValidator");
		priceValidator = (PriceValidator) springContext.getBean("PriceValidator");
		quantityValidator = (QuantityValidator) springContext.getBean("QuantityValidator");
		orderTypeValidator = (OrderTypeValidator) springContext.getBean("OrderTypeValidator");
		sideValidator = (SideValidator) springContext.getBean("SideValidator");
		accountValidator = (AccountValidator) springContext.getBean("AccountValidator");
		
	}

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

	public void validateAndRespond(String accountNumber, String orderType, String side, String symbol, int quantity,
			double price) {
		NewOrderSingle order = new NewOrderSingle();
		order.setAccount(accountNumber);
		char ordType;
		if("LO".equalsIgnoreCase(orderType)) {
			ordType = '2';
		} else if ("ATC".equalsIgnoreCase(orderType)) {
			ordType = '5';
		} else {
			ordType = 'x';
		}
		order.setOrdType(ordType);
		order.setOrderQty(quantity);
		char sd;
		if("B".equalsIgnoreCase(side)) {
			sd = '1';
		} else if ("S".equalsIgnoreCase(side)) {
			sd = '2';
		} else {
			sd = 'x';
		}
		order.setSide(sd);
		order.setSymbol(symbol);
		order.setPrice(price);
		try {
			validate(order);
		} catch (ValidateException e) {
			messagePlaceOrder = "REJECT," + e.getCode();
		}
	}
	
	public String getMessage() {
		return this.messagePlaceOrder;
	}
	
	public void setSession(String session) {
		
		TradingSessionStatus sessionStatus = new TradingSessionStatus();
		sessionStatus.setTradingSessionCode(sessionCode.get(session.toUpperCase()));
		
		ApplicationContext springContext = RobotFrameworkTest.getApplicationContext();
		TradingSessionStatusManager tradingSessionStatusManager = (TradingSessionStatusManager) springContext.getBean("TradingSessionStatusManager");
		tradingSessionStatusManager.setSession(sessionStatus);
	}
	
	
	public void setStockStatus(String symbol, int status) {
		ApplicationContext springContext = RobotFrameworkTest.getApplicationContext();
		SecurityStatusManager securityStatusManager = (SecurityStatusManager) springContext.getBean("SecurityStatusManager");
		List<SecurityStatus> securityStatusList = securityStatusManager.getSecurityStatus();
		for (SecurityStatus securityStatus : securityStatusList) {
			if(securityStatus.getSymbol().equalsIgnoreCase(symbol)) {
				securityStatus.setSecurityTradingStatus(status);
				return;
			}
		}
	}
	
	public void setPrice(String symbol, int floorPrice, int ceilingPrice) {
		ApplicationContext springContext = RobotFrameworkTest.getApplicationContext();
		SecurityStatusManager securityStatusManager = (SecurityStatusManager) springContext.getBean("SecurityStatusManager");
		List<SecurityStatus> securityStatusList = securityStatusManager.getSecurityStatus();
		for (SecurityStatus securityStatus : securityStatusList) {
			if(securityStatus.getSymbol().equalsIgnoreCase(symbol)) {
				securityStatus.setLowPx(floorPrice);
				securityStatus.setHighPx(ceilingPrice);
				return;
			}
		}
	}
}
