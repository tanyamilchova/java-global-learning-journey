package com.example;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PropStreamUtils {

    /*
    Write a Java function Stream<Prop> generate(int n) that and produces stream of n items
    with randomly generated id's and incremental values, starting from 0.
     */
    public static Stream<Prop> generateProps(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> new Prop(UUID.randomUUID(), "PropName" + i, i));

    }


    /*Write a Java function that takes Stream<Prop> and filters it,
     ensuring objects with duplicate id's are removed
     */
    public static List<Prop> removePropsWithDuplicateIds(Stream<Prop> propStream) {
        return propStream
                .filter(prop -> prop.id() != null)
                .collect(Collectors.toMap(
                        prop -> prop.id(),
                        prop -> prop,
                        (existing, duplicate) -> existing
                ))
                .values()
                .stream()
                .toList();
    }


    /*
    Write a Java function that takes Stream<Prop> and returns Map<String, Integer>.
    Keys in the returned map are taken from name property and values are sum of the value proprieties
     */
    public static Map<String, Integer> nameTotalValuesMap(Stream<Prop> propStream) {
        return propStream
                .collect(Collectors
                        .groupingBy(prop -> prop.name()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        (e) -> e.getKey(),
                        (e) -> e.getValue()
                                .stream()
                                .map(prop -> prop.value())
                                .reduce(0, (acc, value) -> acc + value)
                ));

    }


    //  /*
    //    Write a Java function that takes Stream<Prop> and returns a List<Prop> sorted
    //    by value property first then by name.
    //     */
    public static List<Prop> sortProps(Stream<Prop> propStream) {
        return propStream
                .sorted(Comparator
                        .comparingInt(Prop::value)
                        .thenComparing(Prop::name)
                )
                .toList();
    }


    /*
    Write a Java function that takes an ordered Stream<Character> and returns Stream<Character> that contains the first word from the original stream.
    For the purpose of this task, the word is defined as sequence of 1 or more non-whitespace characters.
    */
    public static Stream<Character> getFirstWord(Stream<Character> charSymbol) {
        return charSymbol.takeWhile(character -> character != ' ');
    }


    /*
   Write a Java function that takes Stream<Prop> and returns a Stream<String>.
   The result stream should contains all non-null properties of the objects (e.g. id, name and value).
   */
    public static Stream<String> getNonNullProperties(Stream<Prop> propStream) {
        return propStream
                .flatMap(prop -> Stream.of(
                        prop.id() != null ? prop.id().toString() : null,
                        prop.name() != null ? prop.name() : null,
                        prop.value() != 0 ? String.valueOf(prop.value()) : null
                ))
                .filter(property -> property != null);
    }


    /*
    Write a java funtion that implements 'fold' operation which is defined as applying functions in sequence.
     static <T> Function<T, T> fold(Stream<Function<T, T>> functions){
     */
    public static Function<String, String> fold(Stream<Function<String, String>> functionStream) {
        return functionStream
                .reduce(Function.identity(), Function::andThen);
    }


    /*
  Write a Java function Stream<UUID> toIds(List<List<Prop>>) that returns a stream of all id property values
  of all Prop objects in the supplied structure.
   */
    public static Stream<UUID> toIds(List<List<Prop>> lists) {
        return lists.stream()
                .flatMap(list -> list.stream()
                        .map(prop -> prop.id())
                );
    }


    /*
    Write a Java function that takes Stream<Prop> and returns a List<Prop> where name is not null ,
    */
    public static List<Prop> getNotNullNameProps(Stream<Prop> propStream) {
        return propStream
                .filter(prop -> prop.name() != null)
                .toList();
    }


    /* Write a Java function that takes Stream<Prop> and returns
  the names of the objects which have highest and lowest value property
   */
    public static List<String> nameHighestLowestValue(Stream<Prop> stream) {

        Comparator<Prop> comparator =
                Comparator.comparingInt(Prop::value);

        return stream.collect(
                Collectors.teeing(
                        Collectors.minBy(comparator),
                        Collectors.maxBy(comparator),
                        (min, max) -> List.of(
                                min.orElseThrow().name(),
                                max.orElseThrow().name()
                        )
                )
        );
    }


    /*
    Write a Java function that takes Stream<Integer> and returns number of even numbers.
    */
    public static Integer evenNumbers(Stream<Integer> stream) {
        return (int) stream
                .filter(num -> num % 2 == 0)
                .count();
    }


    /*
   Write a Java function that takes Stream<Prop> and returns information about name conflicts.
   Name conflict is defined as having multiple items with same names but different id's.
   Feel free to pick the resulting data structure as you see fit.
    */
    public static Map<String, List<UUID>> defectNames(List<Prop>propList) {
        Function<Map<String, List<UUID>>, Map<String, List<UUID>>> finisher =
                map -> map.entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)
                        .collect(Collectors.toMap(
                                (e) -> e.getKey(),
                                (e) -> e.getValue()
                        ));

       return propList.stream()
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
    }


    /* Write a Java function that takes Stream<Integer>
    and returns the sums of odd and even numbers.
    */
    public static Map<String, Integer> evenOddSumsMap(Stream<Integer>stream) {
        return stream.collect(Collectors.partitioningBy(intNum -> intNum % 2 == 0))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                (e) -> e.getKey() ? "Even" : "Odd",
                                (e) ->  e.getValue()
                                        .stream()
                                        .reduce(0, (acc, x) -> acc + x)));
    }


    /*
    Write a Java function that takes Stream<Prop> and returns
    the name of the object with the highest value property
     */
    public static String getNameWithHighestValue(Stream<Prop>propStream) {
        return propStream
                        .max(Comparator.comparingInt(Prop::value))
                        .map(prop -> prop.name())
                        .orElse(null);
    }


    /*
    Write a java function that returns a 'partitioning' Collector
    that splits the stream based on the provided predicate
     */
    public static <T, RT, AT, RF, AF, R> Collector<T, ?, R> partitioningCollector(
            Predicate<? super T> predicate,
            Collector<? super T, AT, RT> collTrue,
            Collector<? super T, AF, RF> collFalse,
            BiFunction<RT, RF, R> constructor
    ) {
        record Acc<AT, AF>(AT trueAcc, AF falseAcc) {}

        return Collector.of(
                () -> new Acc<>(collTrue.supplier().get(), collFalse.supplier().get()),

                (acc, element) -> {
                    if (predicate.test(element)) {
                        collTrue.accumulator().accept(acc.trueAcc, element);
                    } else {
                        collFalse.accumulator().accept(acc.falseAcc, element);
                    }
                },

                (a1, a2) -> new Acc<>(
                        collTrue.combiner().apply(a1.trueAcc(), a2.trueAcc()),
                        collFalse.combiner().apply(a1.falseAcc(), a2.falseAcc())
                ),

                acc -> {
                    RT trueResult = collTrue.finisher().apply(acc.trueAcc);
                    RF falseResult = collFalse.finisher().apply(acc.falseAcc);
                    return constructor.apply(trueResult, falseResult);
                }
        );
    }
}
