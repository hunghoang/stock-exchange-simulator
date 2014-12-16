package vn.com.vndirect.exchangesimulator.validator;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.constant.Constants;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("ReplaceQuantityValidator")
public class ReplaceQuantityValidatorImpl implements ReplaceQuantityValidator {

	@Override
	public void validate(int origQty, int qty) throws ValidateException {
		if (origQty >= Constants.LOT_QUANTITY && qty < Constants.LOT_QUANTITY) {
			throw new ValidateException(
					ValidateCode.REPLACE_FROM_EVEN_TO_ODD_NOT_SUPPORTED.code(),
					ValidateCode.REPLACE_FROM_EVEN_TO_ODD_NOT_SUPPORTED.message());
		}

		if (origQty < Constants.LOT_QUANTITY && qty >= Constants.LOT_QUANTITY) {
			throw new ValidateException(
					ValidateCode.REPLACE_FROM_ODD_TO_EVEN_NOT_SUPPORTED.code(),
					ValidateCode.REPLACE_FROM_ODD_TO_EVEN_NOT_SUPPORTED.message());
		}
	}
}
