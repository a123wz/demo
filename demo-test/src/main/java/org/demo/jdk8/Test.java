package org.demo.jdk8;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.zip.DataFormatException;

public class Test {

    public static void main(String[] args) throws Exception{
        /*Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
        Flux.interval(Duration.of(500, ChronoUnit.MILLIS))
                .subscribe(System.out::println);
        //防止程序过早退出，放一个CountDownLatch拦住
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();*/
//        Flux.error(new Exception("some error")).subscribe(System.out::println);
        Mono.empty().subscribe(System.out::println);
        Mono.just(1).flatMap((r) -> {
//            this.routes.put(r.getId(), r);
            return Mono.empty();
        });
//        Flux.just(1, 2, 3, 4, 5, 6).onErrorContinue((s,r)->{
//            System.out.println(s+""+r);
//        }).subscribe(t->{
//                System.out.println(t);
//                if(t==2){
//                    System.out.println(2/0);
//                }
//            });
        Mono.just(new StremTest()).subscribe(t->{
            System.out.println(t);
        });
    }
}
