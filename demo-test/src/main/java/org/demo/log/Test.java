package org.demo.log;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Test {
	private static Logger logger = LoggerFactory.getLogger(Test.class);
	public static void main(String[] args) throws Exception{
//		int i=0;
//		long start = System.currentTimeMillis();
//		while (i<10000) {
//			logger.info("----------i:"+i);
//			i++;
//		}
//		System.out.println(System.currentTimeMillis()-start);

//		DelayQueue<T> delayQueue = new DelayQueue();
//		Long now = System.currentTimeMillis();
//		delayQueue.add(new T("1",now+30*1000));
//		delayQueue.add(new T("2",now+5*1000));
//		delayQueue.add(new T("3",now+15*1000));
//		System.out.println("延迟队列开始时间:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		while (delayQueue.size() != 0) {
//			/**
//			 * 取队列头部元素是否过期
//			 */
//			T task = delayQueue.poll();
//			if (task != null) {
//				System.out.format("{%s} 取消时间:{%s}\n", task.name, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//			}
//			Thread.sleep(1000);
//		}

//		HashedWheelTimer timer = new HashedWheelTimer();
//		System.out.println("延迟队列开始时间:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		timer.newTimeout(new TimerTaskImpl("t1"), 5, TimeUnit.SECONDS);
//		timer.newTimeout(new TimerTaskImpl("t2"), 11, TimeUnit.SECONDS);
//		timer.newTimeout(new TimerTaskImpl("t3"), 5, TimeUnit.SECONDS);
//		timer.newTimeout(new TimerTaskImpl("t4"), 10, TimeUnit.SECONDS);
		Tenum k = Tenum.ONE;
		System.out.println(k.ONE.t());
	}

	@Data
	@AllArgsConstructor
	public static class TimerTaskImpl implements TimerTask{
		private String name;

		@Override
		public void run(Timeout timeout) throws Exception {
			System.out.println("name:"+name+"  \t threadName:"+Thread.currentThread().getName()+"\t date:"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}


	}
	public enum Tenum{
		ONE{
			public String t(){return "1";}
		};
		public String t(){
			throw new RuntimeException("");
		}
	}

	@Data
	public static class T implements Delayed {
		String name;
		private Long extendTime;
		public T(String name,Long extendTime){
			this.name = name;
			this.extendTime = extendTime;
		}

		@Override
		public long getDelay(TimeUnit unit) {
//			System.out.println("111");
			return this.extendTime-System.currentTimeMillis();
		}

		@Override
		public int compareTo(Delayed o) {
			T t = (T)o;
			return this.extendTime-t.extendTime>0?1:-1;
		}
	}
}
