package com.xcy.thread;

/**
 * Created by xuchunyang on 2018/6/26 19点30分
 */
public class JoinDemo {

    public static void main(String[] args) {


        DemoThread thread1 = new DemoThread(3000, "线程1");
        DemoThread thread2 = new DemoThread(10000, "线程2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            System.out.println("线程1join方法执行完成");

            thread2.join();
            System.out.println("线程2join方法执行完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程结束");

    }

}

class DemoThread extends Thread {

    private long time;

    public DemoThread(long time, String name) {
        super(name);
        this.time = time;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"结束");
    }
}
