package com.tianle;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author ：tianLe
 */
@Slf4j
public class TestMainStream {
    public static void main(String[] args) {
//        fileStream();
//         IntStream intStream = IntStream.of(1, 3, 5, 7, 9);
//        final Stream<Integer> boxed = intStream.boxed();
//        boxed.forEach(System.out::println);
//        new Random(10L).ints(5,1,10).forEach(System.out::println);

//        Stream.generate(()->"tianle").limit(10).forEach(System.out::println);
//        Stream.generate(Math::random).limit(5).forEach(System.out::println);
//        Stream.iterate(0,n -> n + 3).limit(10).forEach(System.out::println);
//        Stream.iterate(0,n -> n <= 10,n -> n +2).forEach(System.out::println);

//        flatMap();

//        Stream.of("balance","city","apple","putty")
//                .sorted(Comparator.reverseOrder())
//                .forEach(System.out::println);
//        System.out.println(Stream.of("balance", "city", "apple", "putty").reduce("", (a, b) -> a + b + ";"));

    }

    private static void flatMap() {
        final List<List<String>> lists = List.of(List.of("a", "b", "c"), List.of("e", "f"), List.of("g"));
        final Stream<List<String>> listStream = lists.stream();
//        listStream.forEach(System.out::println);
        final Stream<String> stream = listStream.flatMap(Collection::stream);
        stream.forEach(System.out::println);
    }

    private static void fileStream() {
        // 文件处理
        Path path = Paths.get("file.txt");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            log.error("NoSuchFile", e);
        }
    }
}
