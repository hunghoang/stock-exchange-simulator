package vn.com.vndirect.exchangesimulator.cancel;


public interface Processor<T> {

	void process(T order);

}
