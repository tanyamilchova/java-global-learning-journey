package com.example;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedCollectors {
    /*
    Write a java function that returns a 'partitioning' Collector
    that splits the stream based on the provided predicate
     */

   static  <T, RT, AT, RF, AF, R> Collector<T, ?, R> partitioningCollector(
            Predicate<? super T> predicate,
            Collector<? super T, AT, RT> collTrue,
            Collector<? super T, AF, RF> collFalse,
            BiFunction<RT, RF, R> constructor
    ){
       record Acc<AT, AF>(AT trueAcc, AF falseAcc){}

        return Collector.of(
                () -> new Acc<>(
                        collTrue.supplier().get(),
                        collFalse.supplier().get()
                ),

                (acc, element) -> {
                    if (predicate.test(element)) {
                        collTrue.accumulator()
                                .accept(acc.trueAcc, element);
                    } else {
                        collFalse.accumulator()
                                .accept(acc.falseAcc, element);
                    }
                },

                (a1, a2) -> new Acc<>(
                        collTrue.combiner()
                                .apply(a1.trueAcc(), a2.trueAcc()),
                        collFalse.combiner()
                                .apply(a1.falseAcc(), a2.falseAcc())
                ),

                acc -> {
                    RT trueResult =
                            collTrue.finisher().apply(acc.trueAcc);
                    RF falseResult =
                            collFalse.finisher().apply(acc.falseAcc);
                    return constructor.apply(trueResult, falseResult);
                }
        );
    }

    public static void main(String[] args) {

        String result = Stream.of(1, null, 2, 3, null, 4, 5, 6)
                .collect(
                        partitioningCollector(elem -> elem != null,
                                Collectors.summarizingInt(Integer::intValue),
                                Collectors.counting(),
                                (sum, numberNulls) -> "sum = " + sum.getSum() + ", nulls = " + numberNulls));
        System.out.println(result);


        String resultStr = Stream.of("one", "two_valid", "three", "four_valid", null, "five", null, "eight_valid")
                .collect(
                        partitioningCollector(elem -> elem != null && elem.contains("valid"),
                                Collectors.joining(", "),
                                Collectors.counting(),
                                (validStr, invalidStr) -> "valid = " + validStr +  ",\n invalid count = " + invalidStr));
        System.out.println(resultStr);


        String resultStr1 = Stream.of("one", "two_valid", "three", "four_valid", null, "five", null, "eight_valid")
                .collect(
                        partitioningCollector(elem -> elem != null && elem.contains("valid"),
                                Collectors.joining(", "),
                                Collectors.groupingBy(Objects::nonNull, Collectors.counting()),
                                (validStr, invalidStr) -> "valid = " + validStr +  ",\n invalid count = " + invalidStr));
        System.out.println(resultStr1);
    }
}
