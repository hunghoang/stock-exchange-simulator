package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.Constants;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("QuantityValidator")
public class EvenQuantityValidatorImpl implements QuantityValidator {

	@Override
	public void validate(int quantity) throws ValidateException {
		if (quantity <= 0) {
			throw new ValidateException(ValidateCode.NON_POSITIVE_QUANTITY.code(), ValidateCode.NON_POSITIVE_QUANTITY.message());
		}
		
		if (quantity < Constants.LOT_QUANTITY) {
			throw new ValidateException(ValidateCode.INVALID_EVEN_LOT.code(), ValidateCode.INVALID_EVEN_LOT.message());
		}
		
		if (quantity % Constants.LOT_QUANTITY != 0) {
			throw new ValidateException(ValidateCode.INVALID_LOT.code(), ValidateCode.INVALID_LOT.message());
		}
	}

}
