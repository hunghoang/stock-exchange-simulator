package vn.com.vndirect.exchangesimulator.matching;

import java.security.InvalidParameterException;

public class Sequencer {
	private int seq = 0;
	private String pattern = "";
	private int maxSeq = 0;
	
	public Sequencer(String prefix, int length){
		if(prefix.length() >= length){
			throw new InvalidParameterException("Prefix must be shorter than length");
		}		
		int seqNumLength = length - prefix.length();
		pattern = prefix + "%0" + seqNumLength + "d";
		maxSeq = (int) (Math.pow(10, seqNumLength) - 1);
	}
	
	public String next(){		
		seq++;
		if(seq == maxSeq){
			seq = 0;
		}
		return String.format(pattern, seq);
	}
}
