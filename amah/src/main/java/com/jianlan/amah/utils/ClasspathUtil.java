package com.jianlan.amah.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/6.
 */
public class ClasspathUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathUtil.class);
    private static final String JAVA_CLASS_SUFFIX = ".class";

    public static List<String> scanClassStrList(String packageName) {
        final List<String> classStrList = new LinkedList<String>();
        try {
            final Path classPath = Paths.get(URI.create(Thread.currentThread().getContextClassLoader().getResource(".").toURI().toString()));
            final Path basePath = classPath.resolve(packageName.replace(".", File.separator));
            Files.walkFileTree(basePath, new FileVisitor<Path>() {
                private String basePathStr = classPath.toString();

                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(JAVA_CLASS_SUFFIX)) {
                        String fileName = file.toString();
                        String clsName = fileName.substring(fileName.indexOf(basePathStr) + basePathStr.length() + 1, fileName.lastIndexOf(JAVA_CLASS_SUFFIX)).replaceAll("[/\\\\]", ".");
                        classStrList.add(clsName);
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
        } catch (URISyntaxException e) {
            logger.error("error with classpath uri", e);
        } catch (IOException e) {
            logger.error("error while reading the class file", e);
        }
        return classStrList;
    }
}
