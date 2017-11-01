package org.demo.proxy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.zookeeper.AsyncCallback.StatCallback;

public class TestExe {
	public static void main(String[] args) {
//		test();
		Test t=new Test();
		List<Test> list=new ArrayList<Test>();
		Set<Test> set = new HashSet<Test>();
		list.add(t);
		list.add(t);
		set.add(t);
		set.add(t);
		System.out.println(list.size());
		System.out.println(set.size());
	}
	public static void t(){
		try {
			int i = 1 / 0;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("11111111");
		}
	}
    public static class ExeCallable implements Callable<String>{
    	
    	public String call() throws Exception {
			t();
			return "1";
		}
    }
	public static void test() {
		int threadSum = 5;
		ExecutorService exec = Executors.newFixedThreadPool(threadSum);
		List<Future<String>> taskList = new ArrayList<Future<String>>();
		for (int i = 0; i < threadSum; i++) {
			Future<String> submit = exec.submit(new ExeCallable());
			taskList.add(submit);
		}

		// 依次等待所有task执行完毕
		Iterator<Future<String>> iterator = taskList.iterator();
		while (iterator.hasNext()) {
			Future<String> next = iterator.next();
			try {
				System.out.println("over backdata:" + next.get());
			} catch (InterruptedException e) {
				System.out.println("err:" + e.getMessage());
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("err:" + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("over............");
	}
}
