package vn.com.vndirect.exchangesimulator.hnxexchangesimulator;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:spring-config.xml");
	}
}
