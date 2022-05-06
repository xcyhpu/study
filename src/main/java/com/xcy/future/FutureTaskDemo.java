package com.xcy.future;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author :   xuchunyang
 * @version :   v1.0
 * @date :   2022-05-06 14:11
 */
public class FutureTaskDemo {


    private static final ConcurrentHashMap<String, String> CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, FutureTask<String>> TASK_CACHE = new ConcurrentHashMap<>();


    private static String fetch(String key) {

        // 缓存命中
        final String value = CACHE.get(key);
        if(value != null) {
            System.out.println("缓存命中，直接返回");
            return value;
        }

        FutureTask<String> task = new FutureTask<>(() -> calculate(key));

        FutureTask<String> currentTask = TASK_CACHE.putIfAbsent(key, task);

        // 没有正在执行的任务，执行计算，放入缓存
        if(currentTask == null) {

            System.out.println("跑新任务计算");
            // 跑起来
            task.run();

            // 拿结果
            String calValue;
            try {
                calValue = task.get();
            } catch (Exception e) {
                // handle exception
                e.printStackTrace();
                throw new RuntimeException("计算发生异常1", e);
            }
            CACHE.put(key, calValue);
            return calValue;
        }

        // 有正在执行的任务
        try {
            System.out.println("已有任务在计算，等待计算结果");
            return currentTask.get();
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
            throw new RuntimeException("计算发生异常2", e);
        }

    }


    private static String calculate(String key) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "123->" + key;
    }


    public static void main(String[] args) throws Exception {

        ExecutorService POOL = Executors.newFixedThreadPool(20);

        // 多线程从缓存中取同一个key
        List<Callable<String>> taskList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            taskList.add(() -> fetch("KEY"));
        }

        final List<Future<String>> futures = POOL.invokeAll(taskList);
        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        POOL.shutdown();


        FutureTask<String> task = new FutureTask<>(() -> "1");
        task.run();
        System.out.println(task.get());
        System.out.println(task.get());
        System.out.println(task.get());



    }


}
