import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public class Bootstrap {
    private static final String JAVA_CLASS_SUFFIX = ".class";

    public static Object invoke(String method, Object obj) throws Exception {
        Class<?> cls = obj.getClass();
        Method m = cls.getMethod(method, null);
        return m.invoke(obj, null);
    }

    public static List<String> reverseClass(String basePackage) throws Exception {
        final Path classPath = Paths.get(URI.create(Thread.currentThread().getContextClassLoader().getResource(".").toURI().toString()));
        final Path basePath = classPath.resolve(basePackage.replace(".", File.separator));
        System.out.println(basePath.toString());
        final List<String> clsStrList = new LinkedList<String>();
        Files.walkFileTree(basePath, new FileVisitor<Path>() {
            private String basePathStr = classPath.toString();

            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(JAVA_CLASS_SUFFIX)) {
                    String fileName = file.toString();
                    String clsName = fileName.substring(fileName.indexOf(basePathStr) + basePathStr.length() + 1, fileName.lastIndexOf(JAVA_CLASS_SUFFIX)).replaceAll("[/\\\\]", ".");
                    clsStrList.add(clsName);
                }
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return clsStrList;
    }

    public static void main(String[] args) throws Exception {
        String str = "dfdgdfghfg";
        System.out.println(invoke("toString", str));
        List<String> strings = reverseClass("com.jianlan");
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
