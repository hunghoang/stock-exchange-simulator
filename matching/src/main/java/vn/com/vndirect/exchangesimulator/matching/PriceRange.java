package vn.com.vndirect.exchangesimulator.matching;

public class PriceRange {
	private int stepCount;
	private int floor;
	private int priceStep;


	private int ceil;

	public PriceRange(int floorPrice, int ceilingPrice, int priceStep) {
		this.stepCount = (ceilingPrice - floorPrice) / priceStep + 1;
		this.floor = floorPrice;
		this.ceil = ceilingPrice;
		this.priceStep = priceStep;
	}
	
	private void validatePrice(int price){
		if(price < floor || price > ceil){
			throw new ArrayIndexOutOfBoundsException("Price must be inside floor & ceil: " + price + " should be inside: " + floor + " " + ceil);
		}
	}

	public int getStepCount() {
		return stepCount;
	}

	public int priceToIndex(double price) {
		return priceToIndex((int) Math.round(price));
	}
	
	public int priceToIndex(int price){
		validatePrice(price);
		int index = (price - floor) / priceStep;
		return index;
	}

	public int indexToPrice(int index) {
		return floor + index * priceStep;
	}

	public int getPriceStep() {
		return priceStep;
	}
}
