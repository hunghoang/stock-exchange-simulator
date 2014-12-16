package vn.com.vndirect.exchangesimulator.validator.exception;

public enum ValidateCode {

	INVALID_SYMBOL("-11000", "Chung khoan khong ton tai"),
	HALTED_SYMBOL("-11005", "Chung khoan ngung giao dich (HALT)"),
	CANCELED_SYMBOL("-11007", "Chung khoan huy niem yet"),
	SUSPENDED_SYMBOL("-11013", "Chung khoan dang tam ngung giao dich (SUSPEND)"),
	CANCEL_REPLACE_NOT_SUPPORTED("-12012", "Loai lenh khong cho phep dat lenh huy/sua"), 
	INVALID_SESSION("-13005", "Phien giao dich khong hop le"),
	INVALID_ORDER_TYPE("-13025", "Loai lenh khong hop le"),
	ACCOUNT_MUST_CONTAIN_LETTERS_AND_DIGITS("-14001", "So tai khoan phai chua ca ki tu so va ki tu chu"),
	INVALID_ACCOUNT_FORMAT("-14003", "Ky tu thu 4 cua STK phai thuoc chuoi ky tu {P,C,F}"),
	INVALID_ACCOUNT_LENGTH("-14012", "So tai khoan phai chua 10 ki tu"),
	EMPTY_FIELD("-14013", "Truong du lieu khong duoc de trong"),
	EMPTY_ACCOUNT("-14013", "So tai khoan khong duoc de trong"),
	PRICE_TOO_HIGH("-17002", "Gia phai nho hon hoac bang gia tran"),
	PRICE_TOO_LOW("-17003", "Gia phai lon hon hoac bang gia san"),
	INVALID_PRICE_STEP("-17004", "Gia khong chia het cho buoc gia"),
	INVALID_LOT("-17010", " Khoi luong khong chia het cho lo giao dich"),
	NON_POSITIVE_QUANTITY("-17012", "Khoi luong phai lon hon 0"),
	INVALID_EVEN_LOT("-17015", "Khoi luong giao dich lo chan phai lon hon hoac bang lo giao dich"),
	REPLACE_FROM_EVEN_TO_ODD_NOT_SUPPORTED("-17024", "Khong cho phep sua tu lo chan sang lo le"), 
	REPLACE_FROM_ODD_TO_EVEN_NOT_SUPPORTED("-17027", "Khong cho phep sua tu lo le sang lo chan"),
	UNKNOWN_ORDER("-20001", "Unknown Order"),
	ORIGINAL_ORDER_SYMBOL_MISMATCHED("-20002", "Ma chung khoan khong đung voi thong tin lenh goc"),
	INVALID_SESSION_REPLACE_ORDER("-20006", "Khong cho phep dat lenh sua trong 5' cuoi ATC"),
	INVALID_SESSION_CANCEL_ORDER("-20007", "Khong cho phep dat lenh huy trong 5' cuoi ATC"),
	INVALID_SIDE("-71048", "Side khong dung. Side phai la mua hoac ban"),
	;

	private String code;
	private String message;

	private ValidateCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}
}
