package cn.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static cn.itcast.nio.c1.ByteBufferUtil.debugAll;

@Slf4j
public class TestByteBufferString {

    public static void main(String[] args) {

        String string = "hello";

        /**
         *  1. 字符串转为 ByteBuffer 的第一种方法
         *  String.getBytes() -> byte[] -> ByteBuffer.put(?)
         */
        {
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            byte [] _byte = string.getBytes();
            byteBuffer.put(_byte);
            debugAll(byteBuffer);
        }

        /**
         *  2. 字符串转为 ByteBuffer 的第二种方法
         *  StandardCharsets.UTF_8.encode(String)
         *  自动切换为读模式
         */
        {
            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(string);
            debugAll(byteBuffer);

            /**
             *  2. ByteBuffer 转为字符串的第一种方法
             *  StandardCharsets.UTF_8.decode(ByteBuffer).toString()
             *  Waring: ByteBuffer 需要先切换到读模式，再转字符串才行 !
             */
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            System.out.println(charBuffer.toString());
        }

        /**
         *  3. 字符串转为 ByteBuffer 的第三种方法
         *  String.getBytes() -> byte[] -> ByteBuffer.wrap(?)
         *  自动切换为读模式
         */
        {
            byte [] _byte = string.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(_byte);
            debugAll(byteBuffer);
        }
    }
}
