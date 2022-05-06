package com.xcy.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author :   xuchunyang
 * @version :   v1.0
 * @date :   2022-05-06 15:02
 */
public class DelayQueueDemo {

    static final DelayQueue<DelayItem<String>> QUEUE = new DelayQueue<>();

    static {
        new Thread(() -> {
//            while(!QUEUE.isEmpty()) {
            while(true) {
                try {
                    System.out.println(QUEUE.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            System.out.println("队列空了，exit");
        }).start();
    }

    public static void main(String[] args) throws Exception {

        QUEUE.add(new DelayItem<>("张三", 10000));
        QUEUE.add(new DelayItem<>("李四", 4000));
//        QUEUE.add(new DelayItem<>("王五", 2000));
        // 2秒后，放入一个存活4秒的元素，理论上应该比张三提前轮询
        Thread.sleep(2000);
        QUEUE.add(new DelayItem<>("王五", 4000));


    }


    static class DelayItem<T> implements Delayed {

        /**
         * 值
         */
        private T value;
        /**
         * 存活时间 ms
         */
        private long liveTime;
        /**
         * 死亡时间
         */
        private long deadLine;

        public DelayItem(T value, long liveTime) {
            this.value = value;
            this.liveTime = liveTime;
            this.deadLine = System.currentTimeMillis() + liveTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            // 还要多久死翘翘（能拿出去了）
//            System.out.println(value + "的getDelay方法被调用");
            // 这里为了不刷屏才加的睡眠，实际getDelay方法坚决不能有耗时操作
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return deadLine - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            if(o == null) {
                return 1;
            }
            if(o == this) {
                return 0;
            }
            // 晚死的排后面，避免队列堆积一些该死的东西
            long diff = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
//            diff = -diff;
            return diff > 0 ? 1 : (diff < 0 ? -1 : 0);
        }

        @Override
        public String toString() {
            return "DelayItem{" +
                    "value=" + value +
                    ", liveTime=" + liveTime +
                    '}';
        }
    }


}
