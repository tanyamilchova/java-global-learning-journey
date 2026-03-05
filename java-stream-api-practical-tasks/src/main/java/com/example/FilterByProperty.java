package com.example;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FilterByProperty {
    /*
    Write a Java function that takes Stream<Prop> and returns a List<Prop> where name is not null ,
     */
    public static void main(String[] args) {
        Function<Stream<Prop>, List<Prop>> getNotNullNameProps = propStream -> propStream
                .filter(prop -> prop.name() != null)
                .toList();
        System.out.println(getNotNullNameProps.apply(Stream.of(
                new Prop(null, "Prop1", 1),
                new Prop(null, null, 2),
                new Prop(null, "Prop3", 0),
                new Prop(null, "Prop4",4 )
        )));
    }
}
