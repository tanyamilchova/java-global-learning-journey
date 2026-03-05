package com.example;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

public class Aggregation {
    /*
    Write a Java function that takes Stream<Prop> and returns
    the name of the object with the highest value property
     */
    public static void main(String[] args) {
        Function<Stream<Prop>, String> getNameWithHighestValue = propStream ->
                propStream
                        .max(Comparator.comparingInt(Prop::value))
                        .map(prop -> prop.name())
                        .orElse(null);

        System.out.println(getNameWithHighestValue.apply(Stream.of(
                new Prop(null, "Prop1", 3),
                new Prop(null, "Prop2", 5),
                new Prop(null, "Prop3", 2),
                new Prop(null, "Prop4", 4)
        )));
    }
}
