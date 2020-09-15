package org.demo.jdk8;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通过 stream.sequential() 返回串行的流，通过 stream.parallel() 返回并行的流。
 */
public class StremTest {
    public static void main(String[] args) {
//        sequential();
//        parallel();
//        test();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        System.out.println(String.join(",", list));
    }

    public static void test(){
        //parallelStream 得到的并行流 并行流内部使用了默认的ForkJoinPool（7.2节会进一步讲到分支/合并框架），
        // 它默认的线程数量就是你的处理器数量，这个值是由Runtime.getRuntime().available- Processors()得到的。 但是你可以通过系统属性java.util.concurrent.ForkJoinPool.common. parallelism来改变线程池大小，如下所示： System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","12"); 这是一个全局设置，因此它将影响代码中所有的并行流。反过来说，目前还无法专为某个 并行流指定这个值。一般而言，让ForkJoinPool的大小等于处理器数量是个不错的默认值
        List<String> list = new ArrayList<String>();
        for(int i=0;i<100;i++){
            list.add(""+i);
        }
        list = list.parallelStream().filter(s->{return s.contains("1");}).collect(Collectors.toList());
        //字符串转map
        Map<String,String> map = list.stream().collect(Collectors.toMap(String::toString, a -> a));
        //list 转list
        List<Integer> lis = list.parallelStream().filter(s->{return s.contains("1");}).map(Integer::new).collect(Collectors.toList());
    }
    public static void parallel(){
        List<String> list = new ArrayList<String>();
        for(int i=0;i<1000000;i++){
            double d = Math.random()*1000;
            list.add(d+"");
        }
        long start = System.nanoTime();//获取系统开始排序的时间点
        /**
         * filter()：对元素进行过滤；
         *
         * sorted()：对元素排序；
         *
         * map()：元素的映射；
         *
         * distinct()：去除重复元素；
         *
         * subStream()：获取子 Stream 等。
         */
        int count = (int)((Stream) list.stream().parallel()).sorted().count();
        /**终止操作
         * forEach()：对每个元素做处理；
         *
         * toArray()：把元素导出到数组；
         *
         * findFirst()：返回第一个匹配的元素；
         *
         * anyMatch()：是否有匹配的元素等。
         */
        //以11开始的包含23的字符串
        list.stream().parallel().filter(s->{return s.startsWith("11");}).filter(s->s.indexOf("23")>=0).collect(Collectors.toList()).forEach(System.out::println);
        long end = System.nanoTime();//获取系统结束排序的时间点
        long ms = TimeUnit.NANOSECONDS.toMillis(end-start);//得到并行排序所用的时间
        System.out.println(ms+"ms");
    }
    public static void sequential(){
        List<String> list = new ArrayList<String>();
        for(int i=0;i<1000000;i++){
            double d = Math.random()*1000;
            list.add(d+"");
        }
        long start = System.nanoTime();//获取系统开始排序的时间点
        int count= (int) ((Stream) list.stream().sequential()).sorted().count();
        long end = System.nanoTime();//获取系统结束排序的时间点
        long ms = TimeUnit.NANOSECONDS.toMillis(end-start);//得到串行排序所用的时间
        System.out.println(ms+"ms");
    }
}
