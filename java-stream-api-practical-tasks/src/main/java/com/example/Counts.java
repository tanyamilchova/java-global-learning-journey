package com.example;

import java.util.function.Function;
import java.util.stream.Stream;

public class Counts {
    /*
    Write a Java function that takes Stream<Integer> and returns number of even numbers.
     */
    public static void main(String[] args) {
        Function<Stream<Integer>,Integer> evenNumbers = stream -> ((Long) stream
                .filter(num -> num % 2 == 0)
                .count()).intValue();
        System.out.println(evenNumbers.apply(Stream.of(1,5,8,12,180,45,7)));
    }
}
