package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.Side;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("SideValidator")
public class SideValidatorImpl implements SideValidator {

	@Override
	public void validate(char side) throws ValidateException {
		if (side != Side.SELL.side() && side != Side.BUY.side()) {
			throw new ValidateException(ValidateCode.INVALID_SIDE.code(), ValidateCode.INVALID_SIDE.message());
		}
	}

}
