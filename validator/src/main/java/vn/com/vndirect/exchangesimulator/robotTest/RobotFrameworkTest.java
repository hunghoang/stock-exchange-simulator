package vn.com.vndirect.exchangesimulator.robotTest;

import org.robotframework.RobotFramework;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RobotFrameworkTest {
	private static ApplicationContext SPRING_CONTEXT = new ClassPathXmlApplicationContext("classpath:spring-config.xml");

	public static void main(String[] args) {
		RobotFramework.main(args);
	}

	public static ApplicationContext getApplicationContext() {
		return SPRING_CONTEXT;
	}
}
