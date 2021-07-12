package org.demo.jdk8;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author
 * @version 1.0.0
 * @Description
 * @createTime 2021年07月07日 14:44:00
 */
public class LockTest {
//1.Java并发库中ReetrantReadWriteLock实现了ReadWriteLock接口并添加了可重入的特性
//2.ReetrantReadWriteLock读写锁的效率明显高于synchronized关键字
//3.ReetrantReadWriteLock读写锁的实现中，读锁使用共享模式；写锁使用独占模式，换句话说，读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
//4.ReetrantReadWriteLock读写锁的实现中，需要注意的，当有读锁时，写锁就不能获得；而当有写锁时，除了获得写锁的这个线程可以获得读锁外，其他线程不能获得读锁
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantLock lock1 = null;

    public static void main(String[] args) {
        Thread thread = new Thread();
        thread.yield();
        thread.join();
    }

    static void print(String message) {

    }
}
