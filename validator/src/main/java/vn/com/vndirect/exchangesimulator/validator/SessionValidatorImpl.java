package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.robotTest.RobotFrameworkTest;
import vn.com.vndirect.exchangesimulator.service.SessionService;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("SessionValidator")
public class SessionValidatorImpl implements SessionValidator {

	private SessionService sessionService;

	@Autowired
	public SessionValidatorImpl(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public SessionValidatorImpl() {
		ApplicationContext springContext = RobotFrameworkTest
				.getApplicationContext();
		sessionService = (SessionService) springContext
				.getBean("SessionService");
	}

	@Override
	public void validate(NewOrderSingle order) throws ValidateException {
		if (!sessionService.isLO() && !sessionService.isATC1()
				&& !sessionService.isATC2()) {
			throw new ValidateException(ValidateCode.INVALID_SESSION.code(),
					ValidateCode.INVALID_SESSION.message());
		}

		if (OrderType.ATC.orderType() == order.getOrdType()
				&& sessionService.isLO()) {
			throw new ValidateException(ValidateCode.INVALID_SESSION.code(),
					ValidateCode.INVALID_SESSION.message());
		}

	}

	@Override
	public void validate(OrderCancelRequest request) throws ValidateException {
		if (!sessionService.isLO() && !sessionService.isATC1()) {
			throw new ValidateException(
					ValidateCode.INVALID_SESSION_CANCEL_ORDER.code(),
					ValidateCode.INVALID_SESSION_CANCEL_ORDER.message());
		}
	}

	@Override
	public void validate(OrderReplaceRequest request, char origOrderType) throws ValidateException {
		if(OrderType.ATC.orderType() == origOrderType) {
			throw new ValidateException(ValidateCode.CANCEL_REPLACE_NOT_SUPPORTED.code(), ValidateCode.CANCEL_REPLACE_NOT_SUPPORTED.message());
		}
		
		if (!sessionService.isLO() && !sessionService.isATC1()) {
			throw new ValidateException(ValidateCode.INVALID_SESSION_REPLACE_ORDER.code(),
					ValidateCode.INVALID_SESSION_REPLACE_ORDER.message());
		}
	}
}
