package vn.com.vndirect.exchangesimulator.matching;

import java.util.HashMap;
import java.util.Map;

public class SymbolMatcherFatory {
	
	
    private static Map<String, ContinuousSessionMatcher> matcherMap = new HashMap<String, ContinuousSessionMatcher>();

	public static ContinuousSessionMatcher getMatcher(String symbol) {
		if(matcherMap.containsKey(symbol)) {
			return matcherMap.get(symbol);
		}
		//TODO HARD CODE
		ContinuousSessionMatcher matcher = new ContinuousSessionMatcher(symbol, 13000, 50000, 100);
		matcherMap.put(symbol, matcher);
		
		return matcher;
	}
	
	
}
