package com.xcy.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IOBufferTest {

    public static void main(String[] args) throws IOException {

        byte[] data = "123456789\n".getBytes();
        OutputStream os = new BufferedOutputStream(new FileOutputStream("/root/iotest/out.txt"));
        for (;;) {
            os.write(data);
        }

    }
}
