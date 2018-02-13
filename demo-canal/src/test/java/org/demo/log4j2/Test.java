package org.demo.log4j2;

import org.apache.logging.log4j.LogManager;  
import org.apache.logging.log4j.Logger;  

public class Test {
//	private static Logger logger = LoggerFactory.getLogger(Test.class);
	private static Logger logger = LogManager.getLogger(Test.class.getName());  
	public static void main(String[] args) {
		int i=0;
		logger.info("----------i:"+i);
		System.out.println("ss");
		long start = System.currentTimeMillis();
		while (i<10000) {
			logger.info("----------i:"+i);
			i++;
		}
		System.out.println(System.currentTimeMillis()-start);
	}
}
