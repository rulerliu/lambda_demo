package com.liuwq;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/2 0002 下午 4:25
 * @version: V1.0
 */
public class Test {

    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("tom", 30));
        list.add(new Person("jack", 20));
        list.add(new Person("mike", 25));
        list.add(new Person("jack", 20));
        list.add(new Person("liuwq", 23));

        filter(list);
        distinct(list);
        sorted(list);
        limit(list);
        skip(list);
        limitSkip(list);
        skipLimit(list);
        map(list);

        List<String> list2 = new ArrayList<>();
        list2.add("aaa bbb ccc");
        list2.add("ddd eee fff");
        list2.add("ggg hhh iii");
        flatMap(list2);

        anyMatch(list);
        allMatch(list);
        noneMatch(list);
        reduce(list);
        coount(list);
        printf(list);
        streams();
        mapToInt(list);
        averagingDoubles(list);
        summingInts(list);
        summarizingInts(list);
        maxBys(list);
        joinings(list);
        groupingBys(list);
        partitioningBys(list);

    }

    /**
     * 分区与分组的区别在于，分区是按照 true 和 false 来分的，因此partitioningBy 接受的参数的 lambda 也是 T -> boolean
     *
     * @param list
     */
    private static void partitioningBys(List<Person> list) {
        // 根据年龄是否小于等于20来分区
        Map<Boolean, List<Person>> collect = list.stream().collect(partitioningBy(p -> p.getAge() > 20));
        System.out.println("partitioningBy:" + collect);
    }

    /**
     * groupingBy 用于将数据分组，最终返回一个 Map 类型
     *
     * @param list
     */
    private static void groupingBys(List<Person> list) {
        Map<Integer, List<Person>> collect = list.stream().collect(groupingBy(Person::getAge));
        System.out.println("groupingBy:" + collect);

        // groupingBy 可以接受一个第二参数实现多级分组
        Map<Integer, Map<String, List<Person>>> collect1 = list.stream().collect(groupingBy(Person::getAge, groupingBy(Person::getName)));
        System.out.println("groupingBy:" + collect1);

        // 我们通过年龄进行分组，然后 summingInt(Person::getAge)) 分别计算每一组的年龄总和（Integer），最终返回一个 Map<Integer, Integer>
        Map<Integer, Integer> collect2 = list.stream().collect(groupingBy(Person::getAge, summingInt(Person::getAge)));
        System.out.println("groupingBy:" + collect2);

        Map<Integer, Long> collect3 = list.stream().collect(groupingBy(Person::getAge, counting()));
        System.out.println("groupingBy:" + collect3);

    }

    private static void joinings(List<Person> list) {
        String s = list.stream().map(Person::getName).collect(joining(","));
        System.out.println("joining:" + s);

        String s2 = list.stream().map(Person::getName).collect(joining(",", "Today ", " go shopping"));
        System.out.println("joining:" + s2);
    }

    private static void maxBys(List<Person> list) {
//        Optional<Person> optional = list.stream().collect(maxBy(comparing(Person::getAge)));
        Optional<Person> max = list.stream().max(comparing(Person::getAge));
        Optional<Person> min = list.stream().min(comparing(Person::getAge));
        System.out.println("maxBy:" + max.get());
        System.out.println("maxBy:" + min.get());
    }


    /**
     * @param list
     */
    private static void summarizingInts(List<Person> list) {
        IntSummaryStatistics l = list.stream().collect(summarizingInt(Person::getAge));
        System.out.println("summarizingInt:" + l.getAverage());
        System.out.println("summarizingInt:" + l.getCount());
        System.out.println("summarizingInt:" + l.getMax());
        System.out.println("summarizingInt:" + l.getMin());
    }

    /**
     * 求平均数 averagingDoubles
     *
     * @param list
     */
    private static void averagingDoubles(List<Person> list) {
        Double collect = list.stream().collect(averagingDouble(Person::getAge));
        System.out.println("averagingDouble:" + collect);
    }

    /**
     * 也是计算总和，不过这里需要一个函数参数 summingInt ，summingLong ，summingDouble
     *
     * @param list
     */
    private static void summingInts(List<Person> list) {
        int sum = list.stream().collect(summingInt(Person::getAge));
        System.out.println("summingInt:" + sum);
    }

    /**
     * 流转换为数值流
     * mapToInt(T -> int) : return IntStream
     * mapToDouble(T -> double) : return DoubleStream
     * mapToLong(T -> long) : return LongStream
     * <p>
     * 数值流方法: sum()  max()  min()  average()
     *
     * @param list
     */
    private static void mapToInt(List<Person> list) {
        int sum = list.stream().mapToInt(Person::getAge).sum();
        OptionalInt max = list.stream().mapToInt(Person::getAge).max();
        OptionalInt min = list.stream().mapToInt(Person::getAge).min();
        OptionalDouble average = list.stream().mapToInt(Person::getAge).average();
        System.out.println("mapToInt:" + sum);
        System.out.println("mapToInt:" + max.getAsInt());
        System.out.println("mapToInt:" + min.getAsInt());
        System.out.println("mapToInt:" + average.getAsDouble());
    }

    /**
     * 三种创建流的方式
     */
    private static void streams() {
        System.out.println(">>>创建流:");
        // 1.使用Stream.of创建流
        Stream<String> str = Stream.of("i", "love", "this", "game");
        str.map(String::toUpperCase).forEach(System.out::println);

        // 2.使用数组创建流
        int[] num = {2, 5, 9, 8, 6};
        // 可以stream(num, start, end)指定索引区间
        Arrays.stream(num).forEach(System.out::println);

        // 3.由函数生成流，创建无限流
        // iterate ： 依次对每个新生成的值应用函数
        // generate ：接受一个函数，生成一个新的值

        // 生成流，元素全为 1
        Stream.generate(() -> 1).limit(5).forEach(System.out::println);

        // 生成流，首元素为 0，之后依次加 2
        Stream.iterate(0, n -> n + 2).limit(5).forEach(System.out::println);

        // 生成流，为 0 到 1 的随机双精度数
        Stream.generate(Math :: random).limit(5).forEach(System.out::println);
    }

    /**
     * 打印
     *
     * @param list
     */
    private static void printf(List<Person> list) {
        list.stream().forEach(System.out::println);
    }

    /**
     * 返回流中元素个数，结果为 long 类型 count()
     *
     * @param list
     */
    private static void coount(List<Person> list) {
        long count = list.stream().map(Person::getAge).count();
        System.out.println("count:" + count);
    }

    /**
     * 用于组合流中的元素，如求和，求积，求最大值等 reduce((T, T) -> T) 和 reduce(T, (T, T) -> T)
     *
     * @param list
     */
    private static void reduce(List<Person> list) {
//        int sum = list.stream().map(Person::getAge).reduce(0, (a, b) -> a + b);
        int sum = list.stream().map(Person::getAge).reduce(0, Integer::sum);
        System.out.println("reduce:" + sum);
    }

    /**
     * 流中是否没有元素匹配给定的 T -> boolean 条件 noneMatch(T -> boolean)
     *
     * @param list
     */
    private static void noneMatch(List<Person> list) {
        boolean b = list.stream().noneMatch(p -> p.getName().equals("liuwq"));
        boolean b2 = list.stream().noneMatch(p -> p.getName().equals("sfasdfas"));
        System.out.println("noneMatch:" + b + "," + b2);
    }

    /**
     * 流中是否所有元素都匹配给定的 T -> boolean 条件 allMatch(T -> boolean)
     *
     * @param list
     */
    private static void allMatch(List<Person> list) {
        boolean b = list.stream().allMatch(p -> p.getName().equals("liuwq"));
        System.out.println("allMatch:" + b);
    }

    /**
     * 流中是否有一个元素匹配给定的 T -> boolean 条件 anyMatch(T -> boolean)
     *
     * @param list
     */
    private static void anyMatch(List<Person> list) {
        boolean b = list.stream().anyMatch(p -> p.getName().equals("liuwq"));
        System.out.println("anyMatch:" + b);
    }

    /**
     * 将流中的每一个元素 T 映射为一个流，再把每一个流连接成为一个流 flatMap(T -> Stream)
     *
     * @param list
     */
    private static void flatMap(List<String> list) {
        List<String> collect = list.stream().map(s -> s.split(" ")).flatMap(Arrays::stream).collect(Collectors.toList());
        System.out.println("flatMap:" + collect);
    }

    /**
     * 将流中的每一个元素 T 映射为 R（类似类型转换） map(T -> R)
     *
     * @param list
     */
    private static void map(List<Person> list) {
//        List<String> collect = list.stream().map(p -> p.getName()).collect(Collectors.toList());
        List<String> collect = list.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println("map:" + collect);

        // 遍历修改元素属性(原list也会改变)
        List<Person> collect2 = list.stream().map(p -> {
            if (p.getName().equals("liuwq")) {
                p.setAge(12);
            }
            return p;
        }).collect(Collectors.toList());
        System.out.println("map2:" + collect2);
        System.out.println("list:" + list);
    }

    /**
     * skip(m) 用在 limit(n) 前面时，先去除前 m 个元素再返回剩余元素的前 n 个元素
     *
     * @param list
     */
    private static void skipLimit(List<Person> list) {
        List<Person> collect = list.stream().skip(2).limit(1).collect(Collectors.toList());
        System.out.println("limitSkip:" + collect);
    }

    /**
     * limit(n) 用在 skip(m) 前面时，先返回前 n 个元素再在剩余的 n 个元素中去除 m 个元素
     *
     * @param list
     */
    private static void limitSkip(List<Person> list) {
        List<Person> collect = list.stream().limit(2).skip(1).collect(Collectors.toList());
        System.out.println("limitSkip:" + collect);
    }

    /**
     * 去除前 n 个元素 skip(long n)
     *
     * @param list
     */
    private static void skip(List<Person> list) {
        List<Person> collect = list.stream().skip(2).collect(Collectors.toList());
        System.out.println("skip:" + collect);
    }

    /**
     * 返回前 n 个元素 limit(long n)
     *
     * @param list
     */
    private static void limit(List<Person> list) {
        List<Person> collect = list.stream().limit(2).collect(Collectors.toList());
        System.out.println("limit:" + collect);
    }

    /**
     * 排序 sorted() / sorted((T, T) -> int)
     *
     * @param list
     */
    private static void sorted(List<Person> list) {
//        List<Person> collect = list.stream().sorted((p1, p2) -> p1.getAge() - p2.getAge()).collect(Collectors.toList());
        List<Person> collect = list.stream().sorted(comparing(Person::getAge)).collect(Collectors.toList());
        System.out.println("sorted:" + collect);
    }

    /**
     * 去重 distinct()
     *
     * @param list
     */
    private static void distinct(List<Person> list) {
        List<Person> collect = list.stream().distinct().collect(Collectors.toList());
        System.out.println("distinct:" + collect);
    }

    /**
     * 条件过滤 filter(T -> boolean)
     *
     * @param list
     */
    private static void filter(List<Person> list) {
        List<Person> collect = list.stream().filter(p -> p.getAge() == 20).collect(Collectors.toList());
        System.out.println("filter:" + collect);
    }

}
