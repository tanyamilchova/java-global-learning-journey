package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombiningCollectors {
    /*
    Write a Java function that takes Stream<Prop> and returns information about name conflicts.
    Name conflict is defined as having multiple items with same names but different id's.
    Feel free to pick the resulting data structure as you see fit.
     */
    public static void main(String[] args) {

        UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID id2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID id3 = UUID.fromString("33333333-3333-3333-3333-333333333333");
        List<Prop>propObjects = Stream.of(
                new Prop(id1, "NameProp1", 1),
                new Prop(id1, "NameProp1", 2),
                new Prop(id2, "NameProp1", 3),
                new Prop(id3, "NameProp4", 4 ),
                new Prop(null, "NameProp1", 5)
        ).toList();

       Map<String, List<UUID>> nameConflicts = propObjects.stream()
               .collect(Collectors.groupingBy((prop) -> prop.name()))
               .entrySet()
               .stream()
               .collect(Collectors.toMap(
                       (e) ->e.getKey(),
                       (e) -> e.getValue()
                               .stream()
                               .map(prop -> prop.id() != null ? prop.id() : null)
                               .filter(id -> id != null)
                               .distinct()
                               .collect(Collectors.toList())

               )).entrySet().stream()
               .filter(e -> e.getValue().size() > 1)
               .collect(Collectors.toMap(
                       (e) -> e.getKey(),
                       (e) -> e.getValue()
               ));
        System.out.println(nameConflicts);


        Function<Map<String, List<UUID>>, Map<String, List<UUID>>> finisher =
                map -> map.entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)
                        .collect(Collectors.toMap(
                                (e) -> e.getKey(),
                                (e) -> e.getValue()
                        ));
       Map<String, List<UUID>> defectNames = propObjects.stream()
               .collect(Collectors.collectingAndThen(
                       Collectors.groupingBy(
                               prop -> prop.name(),
                               Collectors.mapping(
                                       prop -> prop.id(),
                                       Collectors.filtering(
                                               prop -> prop != null,
                                               Collectors.collectingAndThen(
                                                       Collectors.toSet(),
                                                       ArrayList::new
                                               )
                                       )
                               )
                       ),
                               finisher));
        System.out.println(defectNames);
    }
}
