package com.example;

import java.util.function.Function;
import java.util.stream.Stream;

public class FunctionalTransformation {
    /*
    Write a java funtion that implements 'fold' operation which is defined as applying functions in sequence.
     static <T> Function<T, T> fold(Stream<Function<T, T>> functions){
     */
    public static void main(String[] args) {
        Function<Stream<Function<String, String>>, Function<String, String>> fold = functionStream ->
                functionStream
                        .reduce(Function.identity(), Function::andThen);

        Function<String,String> f1 = s -> s + "!";
        Function<String,String> f2 = String::toUpperCase;
        Function <String, String> f3 = s -> "[My lovely: " +  s + "]:)";

        System.out.println(fold
                .apply(Stream.of(f1, f2, f3))
                .apply("Happy codding!"));
    }
}
