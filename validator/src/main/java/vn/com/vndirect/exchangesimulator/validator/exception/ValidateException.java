package vn.com.vndirect.exchangesimulator.validator.exception;

@SuppressWarnings("serial")
public class ValidateException extends Exception {

	private String code;
	private String message;

	public ValidateException(String code) {
		this.code = code;
	}

	public ValidateException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ValidateException(String code, String message, Throwable e) {
		super(e);

		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
