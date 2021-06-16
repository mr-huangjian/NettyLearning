package cn.itcast.nio.c1;

public class TestPathsFiles {
    public static void main(String[] args) {
        /**
        {
            Path path = Paths.get("data.txt");
            System.out.println(path.toAbsolutePath());
            System.out.println(path.getFileName());
            System.out.println(path.getFileSystem());
        }

        {
            Path path = Paths.get("/Users/huangjian/.bash_profile");
            System.out.println(path.toAbsolutePath());
            System.out.println(path.getFileName());
            System.out.println(path.getFileSystem());
        }

        {
            // 不会去判断文件是否存在
            // "folder/a/../b" 表示 a 的上级目录 folder 下的 b，即 "folder/b"
            Path path = Paths.get("/Users/huangjian/../.bash_profile");
            System.out.println(path.toAbsolutePath());
            System.out.println(path.normalize());
            System.out.println(path.getFileName());
            System.out.println(path.getFileSystem());

            // 判断文件是否存在
            System.out.println(Files.exists(path));
        }
         */


//        Path path = Paths.get("src/testFolder");
//        try {
//            /*
//             * 创建 "已存在的目录/将新建的目录"
//             * 若目录已存在，抛出异常 java.nio.file.FileAlreadyExistsException
//             * 不能一次创建多级目录，只能创建下一级目录
//             */
//            Files.createDirectory(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        Path path = Paths.get("src/testFolder1/testFolder2");
//        try {
//            /*
//             * 创建 "已存在的目录/将新建的多级目录（包括只一级）"
//             * 若目录已存在，不会抛出异常
//             */
//            Files.createDirectories(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }
}
