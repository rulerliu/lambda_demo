package com.liuwq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/5 0005 上午 10:18
 * @version: V1.0
 */
public class Test2 {

    public static void main(String[] args) {
        System.out.println("主线程名称:" + Thread.currentThread().getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程名称:" + Thread.currentThread().getName() + ">>>传统的方式...");
            }
        }).start();

        new Thread(() -> {
            System.out.println("子线程名称:" + Thread.currentThread().getName() + ">>>lambda的方式1...");
        }).start();

        new Thread(() -> System.out.println("子线程名称:" + Thread.currentThread().getName() + ">>>lambda的方式2...")).start();

        List<Person> list = new ArrayList<>();
        list.add(new Person("tom", 30));
        list.add(new Person("jack", 20));
        list.add(new Person("mike", 25));
        list.add(new Person("jack", 20));
        list.add(new Person("liuwq", 23));
        System.out.println(list);

//        Collections.sort(list, new Comparator<Person>() {
//            @Override
//            public int compare(Person o1, Person o2) {
//                return o1.getAge() - o2.getAge();
//            }
//        });

//        Collections.sort(list, (o1, o2) -> o1.getAge() - o2.getAge());

        Collections.sort(list, Comparator.comparingInt(Person::getAge));

        System.out.println(list);

    }

}
