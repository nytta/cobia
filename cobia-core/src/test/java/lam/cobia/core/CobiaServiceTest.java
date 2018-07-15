package lam.cobia.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.cobia.config.spring.CobiaService;
import lam.cobia.core.constant.Constant;
import lam.cobia.core.service.IMyService;
import lam.cobia.core.service.IYourService;
import lam.cobia.core.service.MyService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public class CobiaServiceTest {
	
	public static void main(String[] args) {
		System.setProperty(Constant.KEY_PORT, "4325");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		context.start();
		
		try {
			IMyService myService = context.getBean("myService", IMyService.class);
			System.out.println(myService);
			
			Object object1 = context.getBean("lam.cobia.core.service.IMyService");
			System.out.println(System.identityHashCode(object1));
			
			IYourService yourService = context.getBean("yourService", IYourService.class);
			System.out.println(yourService);
			
			Object object2 = context.getBean("lam.cobia.core.service.IYourService");
			System.out.println(object2);
			
			Thread.sleep(5000L);
			
			synchronized (CobiaServiceTest.class) {
				CobiaServiceTest.class.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		context.close();
		System.exit(0);
	}

}
