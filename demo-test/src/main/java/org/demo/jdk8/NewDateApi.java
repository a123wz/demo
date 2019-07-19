package org.demo.jdk8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class NewDateApi {

    public static void main(String[] args) {
        //LocalDate
        LocalDate localDate = LocalDate.now(); //获取本地日期
        localDate = LocalDate.ofYearDay(2014, 200); // 获得 2014 年的第 200 天
        System.out.println(localDate.toString());//输出：2014-07-19
        localDate = LocalDate.of(2014, Month.SEPTEMBER, 10); //2014 年 9 月 10 日
        System.out.println(localDate.toString());//输出：2014-09-10
//LocalTime
        LocalTime localTime = LocalTime.now(); //获取当前时间
        System.out.println(localTime.toString());//输出当前时间
        localTime = LocalTime.of(10, 20, 50);//获得 10:20:50 的时间点
        System.out.println(localTime.toString());//输出: 10:20:50
//localDateTime
        LocalDateTime date = LocalDateTime.now();
//        date.atZone(ZoneId.of("Asia/Shanghai"));
        date.atZone(ZoneId.of("Europe/Paris"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");

        System.out.println(date.format(formatter));
        //+1天
        System.out.println(date.plusDays(1).format(formatter));
        //-1天

        System.out.println("10天前的日期 : " + date.minusDays(10));//2018-02-01
        System.out.println("3周前的日期 : " + date.minusWeeks(3));//2018-01-21
        System.out.println("20个月之前的日期 : " + date.minusMonths(20));
        System.out.println("本月第一天的日期 : " + date.with(TemporalAdjusters.firstDayOfMonth()));//2018-02-01
        LocalDateTime lastDayOfYear = date.with(TemporalAdjusters.lastDayOfYear());
        System.out.println("今年最后一天的日期 : " + lastDayOfYear);//

//Clock 时钟
        Clock clock = Clock.systemDefaultZone();//获取系统默认时区 (当前瞬时时间 )
        System.out.println(clock.getZone());
        long millis = clock.millis();//

        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }

    private static void testPeriod() {
        LocalDate date1 = LocalDate.now();
        System.out.println("当前日期 : " + date1);
        LocalDate date2 = date1.plus(3, ChronoUnit.DAYS);
        System.out.println("3天后新日期 : " + date2);
        Period period = Period.between(date1, date2);
        System.out.println("相差 : " + period);//P3D
        System.out.println("相差年份 : " + period.getYears());//0
        System.out.println("相差月份 : " + period.getMonths());//0
        System.out.println("相差天数 : " + period.getDays());//3
    }

    private static void testDuration() {
        LocalTime time1 = LocalTime.now();
        Duration twoHours = Duration.ofHours(2);
        System.out.println("2个小时的秒数 : " + twoHours.getSeconds());
        LocalTime time2 = time1.plus(twoHours);
        System.out.println("2个小时后的新时间 : " + time2);
        Duration duration = Duration.between(time1, time2);
        System.out.println("相差: " + duration);//PT2H
        System.out.println("相差秒数 : " + duration.getSeconds());//7200
    }

    public static void testTemporalAdjusters() {
        LocalDate date1 = LocalDate.now();
        System.out.println("当前日期 : " + date1);
        LocalDate nextTuesday = date1.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        System.out.println("下一个周二 : " + nextTuesday);
        LocalDate firstInMonth = LocalDate.of(date1.getYear(), date1.getMonth(), 1);
        LocalDate secondSaturday = firstInMonth.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).with(
                TemporalAdjusters.next(DayOfWeek.SATURDAY));
        System.out.println("当月的第二个周六 : " + secondSaturday);
    }

}
