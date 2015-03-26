package vn.com.vndirect.exchangesimulator.matching;

public class PriceRange {
	private int stepCount;
	private int floor;
	private int priceStep;


	private int ceil;

	public PriceRange(int floorPrice, int ceilingPrice, int priceStep) {
		this.stepCount = (ceilingPrice - floorPrice) / priceStep + 1;
		this.setFloor(floorPrice);
		this.setCeil(ceilingPrice);
		this.priceStep = priceStep;
	}
	
	private void validatePrice(int price){
		if(price < getFloor() || price > getCeil()){
			throw new ArrayIndexOutOfBoundsException("Price must be inside floor & ceil: " + price + " should be inside: " + getFloor() + " " + getCeil());
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
		int index = (price - getFloor()) / priceStep;
		return index;
	}

	public int indexToPrice(int index) {
		return getFloor() + index * priceStep;
	}

	public int getPriceStep() {
		return priceStep;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getCeil() {
		return ceil;
	}

	public void setCeil(int ceil) {
		this.ceil = ceil;
	}
}
