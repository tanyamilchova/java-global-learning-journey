package com.example;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class IndirectMapping {
    /*
    Write a Java function that takes Stream<Prop> and returns a Stream<String>.
    The result stream should contains all non-null properties of the objects (e.g. id, name and value).
    */
    public static void main(String[] args) {
        Function<Stream<Prop>, Stream<String>> getNonNullProperties = propStream -> propStream
                .flatMap(prop -> Stream.of(
                        prop.id() != null ? prop.id().toString() : null,
                        prop.name() != null ? prop.name() : null,
                        prop.value() != 0 ? String.valueOf(prop.value()) : null
                ))
                .filter(properiy  -> properiy != null )
                ;

        System.out.println(getNonNullProperties.apply(Stream.of(
                new Prop(null, "Prop1", 1),
                new Prop(UUID.randomUUID(), null, 2),
                new Prop(UUID.randomUUID(), "Prop3", 0),
                new Prop(UUID.randomUUID(), "Prop4",4 )
        )).toList());
    }
}
