package vn.com.vndirect.exchangesimulator.datastorage.queue;

public interface QueueService<T> {

	T peek();

	T poll();
	
	boolean add(T obj);

	int size();
}
