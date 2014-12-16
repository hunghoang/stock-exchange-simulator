package vn.com.vndirect.exchangesimulator.datastorage.queue;


public interface QueueInService<T> extends QueueService<T> {
	void route(T t);
}
