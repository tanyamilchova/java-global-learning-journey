package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.function.Predicate;

public class PredicateTest {
    /*
    1. Predicate [10 Marks]
    Write a lambda expression implementation in below test case using Predicate that tests
    whether a string is longer than four characters.
     */
    @Test
    public void a_predicate1() {
        Predicate<String> pred = str12 -> str12.length() > 4;
        Assertions.assertTrue(pred.test("abcde"));
        Assertions.assertFalse(pred.test("abcd"));
        Assertions.assertFalse(pred.test(""));
    }
}
