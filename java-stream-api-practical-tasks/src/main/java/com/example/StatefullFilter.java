package com.example;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatefullFilter {
    /*Write a Java function that takes Stream<Prop> and filters it,
     ensuring objects with duplicate id's are removed
     */
    public static void main(String[] args) {
        Function<Stream<Prop>, List<Prop>> removePropsWithDuplicateIds = propStream ->
                propStream
                        .filter(prop -> prop.id() != null)
                        .collect(Collectors.toMap(
                                prop -> prop.id(),
                                prop -> prop,
                                (existing, duplicate) -> existing
                        ))
                        .values()
                        .stream()
                        .toList();

        UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID id2 = id1;

        System.out.println(removePropsWithDuplicateIds.apply(Stream.of(new Prop(null, "Prop1", 1),
                new Prop(null, "Prop2", 2),
                new Prop(id2, "Prop4", 4),
                new Prop(id1, "Prop3", 3),
                new Prop(UUID.randomUUID(), "Prop5", 5)
        )));
    }
}
