package cn.itcast.nio.test;

import cn.itcast.nio.c1.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");

        Selector boss = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8088));
        ssc.configureBlocking(false);

        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);

        Worker worker = new Worker("worker-0");

        while (true) {
            boss.select();

            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    // ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    log.debug("connected...{}", sc.getRemoteAddress());
                    sc.configureBlocking(false);
                    log.debug("before register...{}", sc.getRemoteAddress());
                    worker.register(sc);
                    // sc.register(worker.selector, SelectionKey.OP_READ, null);
                    log.debug("after register...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {

        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean init = false;
        private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel channel) throws IOException {
            log.debug("channel: {} register start", channel.getRemoteAddress());

            if (init == false) {
                init = true;
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start(); // then, call method 'run()' once
                log.debug("Thread {} start", name);
            }

            /**
             * ???????????????????????????????????????????????????????????????
             * ?????????????????????????????????????????? task.run() ??????????????????????????????
             */
            queue.add(() -> {
                try {
                    // ????????????????????????????????????????????????????????? selector.select() ????????????
                    channel.register(selector, SelectionKey.OP_READ, null);
                    log.debug("channel: {} register finished", channel.getRemoteAddress());
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // ?????? select() ??????????????????????????????
            selector.wakeup();
            log.debug("selector waked");
        }

        @Override
        public void run() {
            log.debug("Thread {} running", name);

            while (true) {
                try {
                    /**
                     * selector ????????? thread ???????????????
                     * ??????????????? run ????????????selector.select() ?????? selector ????????? null?????????????????????
                     */
                    selector.select();

                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            channel.read(buffer);

                            log.debug("read...{}", channel.getRemoteAddress());

                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}



