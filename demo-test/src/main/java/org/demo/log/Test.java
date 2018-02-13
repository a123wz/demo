package org.demo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static Logger logger = LoggerFactory.getLogger(Test.class);
	public static void main(String[] args) {
		int i=0;
		long start = System.currentTimeMillis();
		while (i<10000) {
			logger.info("----------i:"+i);
			i++;
		}
		System.out.println(System.currentTimeMillis()-start);
	}
}
