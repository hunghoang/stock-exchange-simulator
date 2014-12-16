package vn.com.vndirect.exchangesimulator.matching;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;


class CumArray {
	private int[] arr;
	private int size;
	private boolean isIncrease;

	public CumArray(int size, boolean isIncrease) {
		this.size = size;
		arr = new int[size];
		this.isIncrease = isIncrease;
	}

	public void addCum(int pos, int value) {
		int start = isIncrease ? pos : 0;
		int end = isIncrease ? size - 1 : pos;
		for (int i = start; i <= end; i++) {
			arr[i] += value;
		}
	}

	public int get(int i) {
		return arr[i];
	}
}

public class ATCVolAccumulator {
	private CumArray buyCumVol;
	private CumArray sellCumVol;
	private PriceRange priceRange;
	private int count;
	private boolean[] hasLOOrders;

	public ATCVolAccumulator(PriceRange range) {
		this.priceRange = range;
		this.count = priceRange.getStepCount();
		buyCumVol = new CumArray(count, false);
		sellCumVol = new CumArray(count, true);
		hasLOOrders = new boolean[count];
	}


	public int getBuyCumVol(int price) {
		return buyCumVol.get(priceRange.priceToIndex(price));
	}

	public int getSellCumVol(int price) {
		return sellCumVol.get(priceRange.priceToIndex(price));
	}

	private CumArray getTargetCumArray(char c) {
		if (c == NewOrderSingle.BUY) {
			return buyCumVol;
		}

		if (c == NewOrderSingle.SELL) {
			return sellCumVol;
		}

		return null;
	}

	public void add(char c, int price, int vol) {
		int index = priceRange.priceToIndex(price);
		hasLOOrders[index] = true;
		getTargetCumArray(c).addCum(index, vol);
	}

	public void addATC(char c, int vol) {
		int index = -1;
		if (c == NewOrderSingle.BUY) {
			index = count - 1;
		}

		if (c == NewOrderSingle.SELL) {
			index = 0;
		}
		getTargetCumArray(c).addCum(index, vol);
	}

	private int[] matchedVol() {
		int[] result = new int[count];
		for (int i = 0; i < count; i++) {
			result[i] = Math.min(buyCumVol.get(i), sellCumVol.get(i));
		}
		return result;
	}

	private boolean ATCOnly(){
		for (boolean c : hasLOOrders) {
			if(c){
				return false;
			}
		}
		return true;
	}

	private int bestMatchedPosition() {
		if (ATCOnly()) {
			// ATC only
			return -1;
		} else {
			int[] vols = matchedVol();
			int pos = -1;
			int max = 0;
			for (int i = 0; i < count; i++) {
				if (hasLOOrders[i] && max <= vols[i]) {
					max = vols[i];
					pos = i;
				}
			}
			return pos;
		}
	}

	public int getBestPrice() {
		int bestPrice = bestMatchedPosition();
		if (bestPrice == -1) return -1;
		return priceRange.indexToPrice(bestPrice);
	}

	public int getBestVol() {
		return matchedVol()[bestMatchedPosition()];
	}
}
