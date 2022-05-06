package com.xcy.clazz;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author :   xuchunyang
 * @version :   v1.0
 * @date :   2022-04-02 15:44
 */
public class TestJOL {

    public static void main(String[] args) throws InterruptedException {

//        Thread.sleep(5000);

        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }

    }
}
