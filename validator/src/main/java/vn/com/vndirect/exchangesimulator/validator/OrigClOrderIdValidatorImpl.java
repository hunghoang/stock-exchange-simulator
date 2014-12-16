package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("OrigClOrderIdValidator")
public class OrigClOrderIdValidatorImpl implements OrigClOrderIdValidator {

	@Override
	public void validate(String origClOrderId, String clOrderId) throws ValidateException {
		if (!origClOrderId.equals(clOrderId)) {
			throw new ValidateException(ValidateCode.UNKNOWN_ORDER.code(),
					ValidateCode.UNKNOWN_ORDER.message());
		}
	}

}
