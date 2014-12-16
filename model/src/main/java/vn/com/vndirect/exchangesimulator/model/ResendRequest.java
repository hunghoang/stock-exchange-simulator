package vn.com.vndirect.exchangesimulator.model;

public class ResendRequest extends HnxMessage{
	private Integer beginSeqNo;
	private Integer endSeqNo;
	
	public ResendRequest() {
		setMsgType("2");
	}
	
	public int getBeginSeqNo() {
		return beginSeqNo;
	}
	
	public void setBeginSeqNo(int beginSeqNo) {
		this.beginSeqNo = beginSeqNo;
	}
	
	public int getEndSeqNo() {
		return endSeqNo;
	}
	
	public void setEndSeqNo(int endSeqNo) {
		this.endSeqNo = endSeqNo;
	}
	
	
}
