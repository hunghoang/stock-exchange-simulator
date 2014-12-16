package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("OrigSymbolValidator")
public class OrigSymbolValidatorImpl implements OrigSymbolValidator {

	@Override
	public void validate(String origSymbol, String symbol) throws ValidateException {
		if (!origSymbol.equals(symbol)) {
			throw new ValidateException(ValidateCode.ORIGINAL_ORDER_SYMBOL_MISMATCHED.code(),
					ValidateCode.ORIGINAL_ORDER_SYMBOL_MISMATCHED.message());
		}
	}

}
