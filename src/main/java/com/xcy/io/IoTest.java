package com.xcy.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author xuchunyang
 */
public class IoTest {

    public static void main(String[] args) throws IOException {
        byte[] data = "123456789\n".getBytes();
        OutputStream os = new FileOutputStream("/root/iotest/out.txt");
        for (; ; ) {
            os.write(data);
        }

    }
}
