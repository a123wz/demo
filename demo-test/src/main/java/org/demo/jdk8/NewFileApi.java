package org.demo.jdk8;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * BufferedReader.line(): 返回文本行的流 Stream<String>
 *
 * Files.lines(Path, Charset):返回文本行的流 Stream<String>
 *
 * Files.list(Path): 遍历当前目录下的文件和目录
 *
 * Files.walk(Path, int, FileVisitOption): 遍历某一个目录下的所有文件和指定深度的子目录
 *
 * Files.find(Path, int, BiPredicate, FileVisitOption... ): 查找相应的文件
 */
public class NewFileApi {

    public static void main(String[] args) {
        try {
            Files.list(new File(".").toPath())
                    .forEach(System.out::println);
            BufferedReader reader;
        }catch (Exception e){

        }
    }
}
