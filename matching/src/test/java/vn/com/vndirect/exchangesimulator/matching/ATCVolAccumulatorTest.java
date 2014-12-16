package vn.com.vndirect.exchangesimulator.matching;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCVolAccumulatorTest {
	
	private ATCVolAccumulator ac;
	
	@Before
	public void setup(){
		ac = new ATCVolAccumulator(new PriceRange(16000, 16500, 100));
	}

	@Test
	public void emptyCumVolWhenInit(){
		Assert.assertEquals(0, ac.getBuyCumVol(16000));
		Assert.assertEquals(0, ac.getSellCumVol(16500));
	}
	
	@Test
	public void cummulateToLowerBuyingPriceForSingleOrder(){
		ac.add(NewOrderSingle.BUY, 16200, 1000);
		Assert.assertEquals(1000, ac.getBuyCumVol(16000));
		Assert.assertEquals(1000, ac.getBuyCumVol(16200));
		Assert.assertEquals(0, ac.getBuyCumVol(16500));
	}

	@Test
	public void cumToHigherSellPriceForSingleOrder(){
		ac.add(NewOrderSingle.SELL, 16200, 1000);
		Assert.assertEquals(0, ac.getSellCumVol(16000));
		Assert.assertEquals(1000, ac.getSellCumVol(16200));
		Assert.assertEquals(1000, ac.getSellCumVol(16500));
	}
	
	
	@Test
	public void cumToLowerBuyMultiOrder(){
		ac.add(NewOrderSingle.BUY, 16200, 1000);
		ac.add(NewOrderSingle.BUY, 16300, 100);
		Assert.assertEquals(100, ac.getBuyCumVol(16300));
		Assert.assertEquals(1100, ac.getBuyCumVol(16200));
		Assert.assertEquals(0, ac.getBuyCumVol(16500));
	}
	
	@Test
	public void cumBuyATCOrder(){
		ac.add(NewOrderSingle.BUY, 16200, 1000);
		ac.addATC(NewOrderSingle.BUY, 500);
		Assert.assertEquals(1500, ac.getBuyCumVol(16100));
		Assert.assertEquals(1500, ac.getBuyCumVol(16200));
		Assert.assertEquals(500, ac.getBuyCumVol(16500));
	}
	
	@Test
	public void detectBestTradePrice(){
		ac.add(NewOrderSingle.BUY, 16300, 1000);
		ac.add(NewOrderSingle.BUY, 16200, 1000);
		ac.addATC(NewOrderSingle.BUY, 500);
		ac.add(NewOrderSingle.SELL, 16200, 1500);
		ac.add(NewOrderSingle.SELL, 16100, 1500);
		Assert.assertEquals(16200, ac.getBestPrice());
		Assert.assertEquals(2500, ac.getBestVol());
	}
	
	
	@Test
	public void mixATCandLO(){
		ac.addATC(NewOrderSingle.BUY, 2000);
		ac.add(NewOrderSingle.BUY, 16200, 4500);
		ac.addATC(NewOrderSingle.SELL, 1000);
		ac.add(NewOrderSingle.SELL, 16400, 500);
		Assert.assertEquals(16400, ac.getBestPrice());
		Assert.assertEquals(1500, ac.getBestVol());
	}
}
