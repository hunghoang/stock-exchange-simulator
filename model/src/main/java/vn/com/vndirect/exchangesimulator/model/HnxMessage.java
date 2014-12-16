package vn.com.vndirect.exchangesimulator.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class HnxMessage implements Serializable {
	private String beginString;
	private Integer bodyLength;
	private String msgType;
	private String checkSum;
	private String senderCompID;
	private String targetCompID;
	private String sendingTime;
	private Integer LastMsgSeqNumProcessed;
	private Integer msgSeqNum;

	public HnxMessage() {
		setCheckSum("100");
	}
	
	public String getBeginString() {
		return beginString;
	}

	public void setBeginString(String beginString) {
		this.beginString = beginString;
	}

	public int getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public String getSenderCompID() {
		return senderCompID;
	}

	/**
	 * Tag: 49
	 */
	public void setSenderCompID(String senderCompID) {
		this.senderCompID = senderCompID;
	}

	public String getTargetCompID() {
		return targetCompID;
	}

	/**
	 * 56
	 */
	public void setTargetCompID(String targetCompID) {
		this.targetCompID = targetCompID;
	}

	public String getSendingTime() {
		return sendingTime;
	}

	public void setSendingTime(String sendingTime) {
		this.sendingTime = sendingTime;
	}

	public int getLastMsgSeqNumProcessed() {
		return LastMsgSeqNumProcessed;
	}

	public void setLastMsgSeqNumProcessed(int lastMsgSeqNumProcessed) {
		LastMsgSeqNumProcessed = lastMsgSeqNumProcessed;
	}

	public int getMsgSeqNum() {
		return msgSeqNum;
	}

	public void setMsgSeqNum(int msgSeqNum) {
		this.msgSeqNum = msgSeqNum;
	}

}
