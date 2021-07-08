package org.demo.jdk8;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年07月07日 17:22:00
 */
@Slf4j
public class UnsafeTest {
//    https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html

    public UnsafeTest(){

    }

    public Integer state;

    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private volatile Integer i;
    public static void main(String[] args) throws Exception{
        Unsafe unsafe = reflectGetUnsafe();
        print(ClassLayout.parseInstance(UnsafeTest.class).toPrintable());
        Long stateOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("state"));
        System.out.println(unsafe.getObject(UnsafeTest.class,stateOffset));
//        unsafe.putObject(UnsafeTest.class,stateOffset,1);
//        System.out.println(unsafe.getObject(UnsafeTest.class,stateOffset));
//        boolean update = unsafe.compareAndSwapLong(UnsafeTest.class, stateOffset, 0, 8886688888L);
//        boolean update1 = unsafe.compareAndSwapInt(UnsafeTest.class, stateOffset, 1, 2);
//        System.out.println("stateOffset:"+stateOffset+"\t update:"+update+" \t"+update1);
        UnsafeTest t = new UnsafeTest();
//        Class cla = Class.forName("org.demo.jdk8.UnsafeTest");
//        t = (UnsafeTest) cla.newInstance();
//        unsafe.putObject(t,stateOffset,1);
        System.out.println(t.state);
        //查看对象内部信息
        print(ClassLayout.parseInstance(t).toPrintable());

        //查看对象外部信息
//        print(GraphLayout.parseInstance(t).toPrintable());

        //获取对象总大小
//        print("size : " + GraphLayout.parseInstance(t).totalSize());
    }

    static void print(String message) {
        System.out.println(message);
        System.out.println("-------------------------");
    }
}
