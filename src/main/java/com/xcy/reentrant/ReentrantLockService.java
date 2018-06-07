package com.xcy.reentrant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuchunyang on 2018/6/7 11点58分
 */
public class ReentrantLockService {

    static class ComputeTask implements Runnable {

        private long time;

        private CountDownLatch lock;

        private String name;

        public ComputeTask(long time, CountDownLatch lock, String name) {
            this.time = time;
            this.lock = lock;
            this.name = name;
        }

        @Override
        public void run() {

            System.out.println("Thread："+name+" start work");

            try {
                Thread.sleep(time);
                System.out.println("Thread："+name+" finish work");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.countDown();
            }

        }
    }


    public static void main(String[] args) {

        final CountDownLatch lock = new CountDownLatch(4);

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(30, false));

        final ComputeTask task1 = new ComputeTask(2000, lock, "任务1");
        final ComputeTask task2 = new ComputeTask(1000, lock, "任务2");
        final ComputeTask task3 = new ComputeTask(5000, lock, "任务3");
        final ComputeTask task4 = new ComputeTask(3000, lock, "任务4");

        threadPool.execute(task1);
        threadPool.execute(task2);
        threadPool.execute(task3);
        threadPool.execute(task4);

        try {
            System.out.println("等待四个任务执行完");
            lock.await();
            System.out.println("执行完了。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
