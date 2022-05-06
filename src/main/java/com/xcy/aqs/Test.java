package com.xcy.aqs;

import sun.misc.Unsafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author :   xuchunyang
 * @version :   v1.0
 * @date :   2022-04-01 16:08
 */
public class Test {

    private static final class MySync extends AbstractQueuedSynchronizer {

        int count;

        public MySync(int count) {
            setState(count);
        }

        public int getCount() {
            return getState();
        }

        public boolean compareAndSet(int expectValue, int newValue) {
            return compareAndSetState(expectValue, newValue);
        }

    }

    public static void main(String[] args) throws NoSuchFieldException {

        final MySync mySync = new MySync(1);
        System.out.println(mySync.compareAndSet(3, 2));
        System.out.println(mySync.compareAndSet(1, 2));
        System.out.println(mySync.compareAndSet(1, 3));
        System.out.println(mySync.compareAndSet(2, 3));
    }
}
