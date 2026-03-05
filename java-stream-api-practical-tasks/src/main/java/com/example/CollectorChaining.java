package com.example;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorChaining {
    /* Write a Java function that takes Stream<Integer>
    and returns the sums of odd and even numbers.
     */
    public static void main(String[] args) {
        Function<Stream<Integer>, Map<String, Integer>> evenOddSumsMap = str ->
            str.collect(Collectors.partitioningBy(intNum -> intNum % 2 == 0))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            (e) -> e.getKey() ? "Even" : "Odd",
                            (e) ->  e.getValue()
                            .stream()
                            .reduce(0, (acc, x) -> acc + x)));

        System.out.println(evenOddSumsMap.apply(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
    }
}
