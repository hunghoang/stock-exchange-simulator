package vn.com.vndirect.exchangesimulator.model;

public class SequenceReset extends HnxMessage{
	private Integer newSeqNo;

	public SequenceReset() {
		setMsgType("4");
	}
	
	public int getNewSeqNo() {
		return newSeqNo;
	}

	public void setNewSeqNo(int newSeqNo) {
		this.newSeqNo = newSeqNo;
	}
	
}
