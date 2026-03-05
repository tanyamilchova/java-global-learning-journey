package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class Sorting {
    /*
    Write a Java function that takes Stream<Prop> and returns a List<Prop> sorted
    by value property first then by name.
     */
    public static void main(String[] args) {
        Function <Stream<Prop>, List<Prop> > sortProps = propStream -> propStream
                .sorted(Comparator
                        .comparingInt(Prop::value)
                        .thenComparing(Prop::name)
                )
                .toList();

        System.out.println(sortProps.apply(Stream.of(
                new Prop(UUID.randomUUID(), "AProp1", 3),
                new Prop(UUID.randomUUID(), "ZProp2", 1),
                new Prop(UUID.randomUUID(), "BProp3", 2),
                new Prop(UUID.randomUUID(), "KProp4", 1),
                new Prop(UUID.randomUUID(), "KProp4", 1)
        )));
    }
}
