package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.robotTest.RobotFrameworkTest;
import vn.com.vndirect.exchangesimulator.service.SecurityService;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("PriceValidator")
public class PriceValidatorImpl implements PriceValidator {
	
	private static final int PRICE_STEP = 100;
	
	private SecurityService securityService;
	
	@Autowired
	public PriceValidatorImpl(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	public PriceValidatorImpl() {
		ApplicationContext springContext = RobotFrameworkTest.getApplicationContext();
		securityService = (SecurityService) springContext.getBean("SecurityService");
	}

	@Override
	public void validate(String symbol, double price) throws ValidateException {
		SecurityStatus security = securityService.getSecurityBySymbol(symbol);
		if (security != null) {
			if (price < security.getLowPx()) {
				throw new ValidateException(ValidateCode.PRICE_TOO_LOW.code(), ValidateCode.PRICE_TOO_LOW.message());
			}
			
			if (price > security.getHighPx()) {
				throw new ValidateException(ValidateCode.PRICE_TOO_HIGH.code(), ValidateCode.PRICE_TOO_HIGH.message());
			}
			
			if (price % PRICE_STEP != 0) {
				throw new ValidateException(ValidateCode.INVALID_PRICE_STEP.code(), ValidateCode.INVALID_PRICE_STEP.message());
			}
		}
	}

}
