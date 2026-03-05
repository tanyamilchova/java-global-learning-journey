package com.example;

import java.util.function.Function;
import java.util.stream.Stream;

public class ShorteningOperations {
    /*
    Write a Java function that takes an ordered Stream<Character> and returns Stream<Character> that contains the first word from the original stream.
      For the purpose of this task, the word is defined as sequence of 1 or more non-whitespace characters.
     */
    public static void main(String[] args) {
        Function<Stream<Character>, Stream<Character>> getFirstWord = charSymbol ->
            charSymbol.takeWhile(character -> character != ' ');
        System.out.println(getFirstWord.apply(Stream.of('B', 'a', 'l', 'l', ' ', 'F', 'u', 'n')).toList());
    }
}
