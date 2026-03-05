package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomAggregation {
    /* Write a Java function that takes Stream<Prop> and returns
    the names of the objects which have highest and lowest value property
     */
    public static void main(String[] args) {
        Function<Stream<Prop>, List<String>> nameHighestLowestValue =
                stream -> {

                    Comparator<Prop> comparator =
                            Comparator.comparingInt(Prop::value);

                    var result = stream.collect(
                            Collectors.teeing(
                                    Collectors.minBy(comparator),
                                    Collectors.maxBy(comparator),
                                    (min, max) -> List.of(
                                            min.orElseThrow().name(),
                                            max.orElseThrow().name()
                                    )
                            )
                    );
                    return result;
                };

        System.out.println(nameHighestLowestValue.apply(Stream.of(
                new Prop(null, "Prop1", 3),
                new Prop(null, "Prop2", 5),
                new Prop(null, "Prop3", 2),
                new Prop(null, "Prop4", 0)
        )));
    }
}
