package vn.com.vndirect.exchangesimulator.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.OrderType;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("OrderTypeValidator")
public class OrderTypeValidatorImpl implements OrderTypeValidator {

	@Override
	public void validate(char ordType) throws ValidateException {
		if (StringUtils.isEmpty(String.valueOf(ordType))) {
			throw new ValidateException(ValidateCode.INVALID_ORDER_TYPE.code(),
					ValidateCode.INVALID_ORDER_TYPE.message());
		}

		if (ordType != OrderType.LO.orderType()
				&& ordType != OrderType.ATC.orderType()
				&& ordType != OrderType.MOK.orderType()
				&& ordType != OrderType.MAK.orderType()
				&& ordType != OrderType.MTL.orderType()) {
			throw new ValidateException(ValidateCode.INVALID_ORDER_TYPE.code(),
					ValidateCode.INVALID_ORDER_TYPE.message());
		}
	}

}
