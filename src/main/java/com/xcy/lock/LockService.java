package com.xcy.lock;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ht_admin on 2018/5/24.
 */
public class LockService {

    public static final AtomicInteger counter = new AtomicInteger(1);

    public static void main(String[] args) {


        Pool pool = new Pool();

        Producer p1 = new Producer(pool);
        Producer p2 = new Producer(pool);
        Producer p3 = new Producer(pool);

        Consumer c1 = new Consumer(pool);
        Consumer c2 = new Consumer(pool);
        Consumer c3 = new Consumer(pool);
        Consumer c4 = new Consumer(pool);


        new Thread(p1, "生产者1").start();
        new Thread(p2, "生产者2").start();
        new Thread(p3, "生产者3").start();

        new Thread(c1, "消费者1").start();
        new Thread(c2, "消费者2").start();
        new Thread(c3, "消费者3").start();
        new Thread(c4, "消费者4").start();

//        Random random = new Random();
//
//        for(int i=0;i<10;i++)
//            System.out.println(random.nextInt(10)+1);
    }

}


class Pool {

    private static final int SIZE = 10;

    private LinkedList<String> list = new LinkedList<String>();

    private Lock lock = new ReentrantLock();

    final Condition notFull = lock.newCondition();

    final Condition notEmpty = lock.newCondition();

    public void add(String item) {

        String name = Thread.currentThread().getName();

        lock.lock();

        try {

            while(list.size()>=SIZE) {

                System.out.println(name + " :  池子满了等待");

                /** 等待池子不满的消息 */
                notFull.await();

            }

            list.addLast(item);

            System.out.println(name + " : 生产 ：" +item);

            /** 池子不空了 */
            notEmpty.signalAll();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } finally {

            lock.unlock();

        }

    }

    public void del() {

        String name = Thread.currentThread().getName();

        lock.lock();

        try {
            while(list.isEmpty()) {

                System.out.println(name + " :  池子空了等待");

                /** 等待池子不空的消息 */
                notEmpty.await();
            }

            String item = list.removeFirst();
            
            System.out.println(name + " : 消费 ：" +item);

            /** 池子不满了 */
            notFull.signalAll();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } finally {

            lock.unlock();

        }


    }

}

class Producer implements Runnable {

    private Random random = new Random();

    private Pool pool;

    public Producer(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {

        while(true) {

            pool.add("Item - " + LockService.counter.getAndAdd(1));

            try {
                Thread.sleep(100 * (random.nextInt(10)+1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

class Consumer implements Runnable {

    private Random random = new Random();

    private Pool pool;

    public Consumer(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {

        while(true) {

            pool.del();

            try {
                Thread.sleep(100 * (random.nextInt(10)+1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
