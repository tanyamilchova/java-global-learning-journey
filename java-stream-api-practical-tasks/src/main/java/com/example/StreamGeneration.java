package com.example;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamGeneration {
    /*
    Write a Java function Stream<Prop> generate(int n) that and produces stream of n items
    with randomly generated id's and incremental values, starting from 0.
     */
    public static void main(String[] args) {
    Function<Integer, Stream<Prop>> generate = n -> IntStream.range(0, n)
            .mapToObj(i -> new Prop(UUID.randomUUID(), "PropName" + i, i));
        System.out.println(generate.apply(5).toList());
    }
}
