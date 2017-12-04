package org.demo.canal;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class Test {
	public static void test(String path) throws IOException{
		Set<Resource> result = new LinkedHashSet<Resource>(16);
		ClassLoader cl = new DefaultResourceLoader().getClassLoader();
//		ClassLoader cl = getClassLoader();
		Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path));
		while (resourceUrls.hasMoreElements()) {
			URL url = resourceUrls.nextElement();
			System.out.println(url.getPath());
//			Class cla = Class.forName(className)
//			result.add(convertClassLoaderURL(url));
		}
	}
	static public class T{
		String str;
		public T(String str){
			this.str=str;
		}
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}
//		@Override
//		public boolean equals(Object obj) {
//			// TODO Auto-generated method stub
//			return super.equals(obj);
//		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "11";
		}
		
	}
	public static void main(String[] args) throws Exception {
		
	}
}
