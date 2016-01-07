package vn.com.vndirect.exchangesimulator.builder;

import java.util.ArrayList;
import java.util.List;

public class FixMessageBuilder {

	private StringBuilder buffer = new StringBuilder();

	public List<String> build(byte[] bytes) {
		List<String> messages = new ArrayList<String>();
		buffer.append(new String(bytes));

		int checkSumIndex = buffer.indexOf("10=");

		while (checkSumIndex > 0) {
			int beginMessageIndex = buffer.indexOf("8=FIX.4.49=");
			int endOfMessageIndex = buffer.indexOf("", checkSumIndex + 1) + 1;
			String message = buffer.substring(beginMessageIndex, endOfMessageIndex);
			buffer.delete(beginMessageIndex, endOfMessageIndex);
			boolean isValidMessage = validateLength(message, checkSumIndex);
			if (isValidMessage) {
				messages.add(message);
			}
			checkSumIndex = buffer.indexOf("10=");
		}

		if (checkSumIndex < 0)
			return messages;
		return messages;

	}

	private boolean validateLength(String message, int checkSumIndex) {
		return true;
	}

}
