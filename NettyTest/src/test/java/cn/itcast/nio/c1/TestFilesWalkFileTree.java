package cn.itcast.nio.c1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFilesWalkFileTree {
    public static void main(String[] args) throws IOException {
        // 导入包和抛出方法异常，快捷键：Alt + Enter
        // 弹出所有方法，快捷键：Ctrl + O
        // 运行 debug 模式下，动态调用对象的某个表达式，快捷键：Alt + F8  (Evaluate Expression)

        // 疑问：AtomicInteger 与 int 有啥区别？为什么这里不能用 int ?

        final AtomicInteger dirCount = new AtomicInteger();
        final AtomicInteger fileCount = new AtomicInteger();

        Files.walkFileTree(Paths.get("/Volumes/Data/GitLab/SDK-Native/GFSDKMainland_Main/GameFriendSDKForRN/GameFriendSDK/node_modules/@sdk-react-native/utils"), new SimpleFileVisitor<Path>() {
            @Override // 进入文件夹前
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // System.out.println("dir: " + dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override // 访问该文件
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".js")) {
                    System.out.println("js: " + file);
                }

                // System.out.println("\t " + file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }

            @Override // 访问该文件失败
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return super.visitFileFailed(file, exc);
            }

            @Override // 退出文件夹后
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return super.postVisitDirectory(dir, exc);
            }
        });

        System.out.println("dirCount: " + dirCount);
        System.out.println("fileCount: " + fileCount);
    }
}
