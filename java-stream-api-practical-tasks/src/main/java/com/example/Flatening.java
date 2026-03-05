package com.example;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class Flatening {
    /*
    Write a Java function Stream<UUID> toIds(List<List<Prop>>) that returns a stream of all id property values
    of all Prop objects in the supplied structure.
     */
    public static void main(String[] args) {
        Function<List<List<Prop>>, Stream<UUID>> toIds = lists -> lists.stream()
                .flatMap(list -> list.stream()
                        .map(prop ->prop.id())
                );
        List<List<Prop>> listListProp = List.of(
                List.of(new Prop(UUID.randomUUID(), "Prop1", 1), new Prop(UUID.randomUUID(), "Prop2", 2)),
                List.of(new Prop(UUID.randomUUID(), "Prop3", 3), new Prop(UUID.randomUUID(), "Prop4", 4))
        );

        Stream<UUID>uuidStream = toIds.apply(listListProp);
        System.out.println(uuidStream.toList());
    }
}
