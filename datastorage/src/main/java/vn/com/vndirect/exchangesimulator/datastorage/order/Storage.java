package vn.com.vndirect.exchangesimulator.datastorage.order;

import java.util.List;

public interface Storage<T> {
	void add(T order);

	boolean remove(String key);

	List<T> remain();

	T get(String key);

	int size();

	boolean isEmpty();
}
