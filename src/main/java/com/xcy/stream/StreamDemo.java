package com.xcy.stream;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by xuchunyang on 2018/6/7 10点38分
 */
public class StreamDemo {

    public static void main(String[] args) {


        Runnable worker = () -> System.out.println("haha");

        new Thread(worker).start();


        List<String> list = Lists.newArrayList("z","m","a","k","x");


        System.out.println("输出>n的元素");

        list.stream()
                .filter(s -> {return s.compareTo("n")>0;})
                .forEach(e -> {System.out.println(e);});

        System.out.println("全部转换为大写");

        list.stream()
                .map(item -> {return item.toUpperCase();})
                .forEach(item -> {
                    System.out.println(item);
                });

        List<Person> persons = Lists.newArrayList(new Person(15, "Tom", 1), new Person(28, "David", 2), new Person(40, "Lucy", 2));

        System.out.println("排序");

        persons.stream()
                .sorted(Comparator.comparing(Person::getSex).reversed().thenComparing((p1, p2) -> {return p1.getAge()-p2.getAge();}))
                .forEach(item -> {
                    System.out.println(item);
                });


        System.out.println("collect用法");

        final List<String> collect = list.stream()
                .map(item -> {
                    return item.toUpperCase();
                })
                .collect(Collectors.toList());
        System.out.println(Arrays.toString(collect.toArray()));

        System.out.println("list转map");

        final Map<String, Person> map = persons.stream()
                .sorted(Comparator.comparing(Person::getSex).thenComparing(Person::getAge))
                .collect(Collectors
                        .toMap(Person::getName, person -> person));

        System.out.println(map);


        System.out.println(Arrays.toString(list.toArray()));

        Collections.sort(list, (a, b) -> {return b.compareTo(a);});

        System.out.println(Arrays.toString(list.toArray()));

        List<Integer> list2 = Stream.of(1,2,4,5,6,7,8).collect(Collectors.toList());

        final Integer reduce = list2.stream().reduce(1, (prev, next) -> {
            return prev * next;
        });

        System.out.println("累乘结果："+reduce);

        final Integer reduce2 = list2.stream().reduce(0, (prev, next) -> {
            return prev + next;
        });

        System.out.println("累加结果："+reduce2);


        System.out.println("简单的Map-Reduce");

        List<String> stringList = Lists.newArrayList("H","e","l","l","o","\t","W","o","r","l","d");

        final String concat = stringList.stream()
                .map(string -> {return string.toUpperCase();})
                .reduce("Prefix_-_-", (prev, next) -> {
                    return prev + next;
                });

        System.out.println("字符串简单拼接："+concat);



    }

    private static class Person {

        private int age;

        private String name;

        private int sex;

        public Person(int age, String name, int sex) {
            this.age = age;
            this.name = name;
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
