package com.example;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatefulCollectors {
    /*
    Write a Java function that takes Stream<Prop> and returns Map<String, Integer>.
    Keys in the returned map are taken from name property and values are sum of the value propertiesps
     */
    public static void main(String[] args) {
        Function<Stream<Prop>, Map<String, Integer>> nameTotalValuesMap = propStream -> propStream
                .collect(Collectors.groupingBy(prop ->
                        prop.name()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        (e) -> e.getKey(),
                        (e) -> e.getValue()
                                .stream()
                                .map(prop -> prop.value())
                                .reduce(0, (acc, value) -> acc + value)
                ));
        System.out.println(nameTotalValuesMap.apply(Stream.of(
                new Prop(null, "NameProp1", 3),
                new Prop(null, "NameProp1", 10),
                new Prop(null, "NameProp2", 5),
                new Prop(null, "NameProp2", 20),
                new Prop(null, "NameProp3", 2),
                new Prop(null, "NameProp4", 4)
        )));
    }
}
