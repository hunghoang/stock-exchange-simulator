package vn.com.vndirect.exchangesimulator.validator;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

@Component("AccountValidator")
public class AccountValidatorImpl implements AccountValidator {

	private static final String FOURTH_CHARACTER_RESTRICTION_REGEX = "^.{3}[pcf]";
	private static final String MUST_CONTAIN_LETTER_AND_DIGIT_REGEX = "^([0-9]+[a-zA-Z]+|[a-zA-Z]+[0-9]+)[0-9a-zA-Z]*$";
	private static final int ACCOUNT_LENGTH = 10;

	@Override
	public void validate(String account) throws ValidateException {
		if (StringUtils.isEmpty(account)) {
			throw new ValidateException(ValidateCode.EMPTY_ACCOUNT.code(), ValidateCode.EMPTY_ACCOUNT.message());
		}

		if (account.length() != ACCOUNT_LENGTH) {
			throw new ValidateException(ValidateCode.INVALID_ACCOUNT_LENGTH.code(),
					ValidateCode.INVALID_ACCOUNT_LENGTH.message());
		}

		if (!account.matches(MUST_CONTAIN_LETTER_AND_DIGIT_REGEX)) {
			throw new ValidateException(ValidateCode.ACCOUNT_MUST_CONTAIN_LETTERS_AND_DIGITS.code(),
					ValidateCode.ACCOUNT_MUST_CONTAIN_LETTERS_AND_DIGITS.message());
		}
		
		Pattern pattern = Pattern.compile(FOURTH_CHARACTER_RESTRICTION_REGEX, Pattern.CASE_INSENSITIVE);
		if (!pattern.matcher(account).find()) {
			throw new ValidateException(ValidateCode.INVALID_ACCOUNT_FORMAT.code(),
					ValidateCode.INVALID_ACCOUNT_FORMAT.message());
		}
	}

}
