package vn.com.vndirect.exchangesimulator.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.SecurityTradingStatus;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.robotTest.RobotFrameworkTest;
import vn.com.vndirect.exchangesimulator.service.SecurityService;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("SymbolValidator")
public class SymbolValidatorImpl implements SymbolValidator {

	private SecurityService securityService;

	@Autowired
	public SymbolValidatorImpl(SecurityService securityService) {
		this.securityService = securityService;
	}

	public SymbolValidatorImpl() {
		ApplicationContext springContext = RobotFrameworkTest.getApplicationContext();
		securityService = (SecurityService) springContext.getBean("SecurityService");
	}
	@Override
	public void validate(String symbol) throws ValidateException {
		if (StringUtils.isEmpty(symbol)) {
			throw new ValidateException(ValidateCode.EMPTY_FIELD.code(), ValidateCode.EMPTY_FIELD.message());
		}

		SecurityStatus security = securityService.getSecurityBySymbol(symbol);
		if (security == null) {
			throw new ValidateException(ValidateCode.INVALID_SYMBOL.code(), ValidateCode.INVALID_SYMBOL.message());
		}

		if (security.getSecurityTradingStatus() == SecurityTradingStatus.CANCELED.status()) {
			throw new ValidateException(ValidateCode.CANCELED_SYMBOL.code(), ValidateCode.CANCELED_SYMBOL.message());
		} else if (security.getSecurityTradingStatus() == SecurityTradingStatus.SUSPENDED.status()) {
			throw new ValidateException(ValidateCode.SUSPENDED_SYMBOL.code(), ValidateCode.SUSPENDED_SYMBOL.message());
		} else if (security.getSecurityTradingStatus() == SecurityTradingStatus.HALTED.status()) {
			throw new ValidateException(ValidateCode.HALTED_SYMBOL.code(), ValidateCode.HALTED_SYMBOL.message());
		}
	}
}
