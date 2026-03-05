package test;

import org.junit.Test;
import java.util.function.Function;
import static org.junit.Assert.assertEquals;

public class FunctionTest {
    /*
    2. Function [10 Marks]
    Write a lambda expression that wraps the given string in parentheses.
     */
    @Test
    public void function1() {
        Function<String, String> func =
                str -> "(" + str + ")";

        assertEquals("(abc)", func.apply("abc"));
        assertEquals("()", func.apply(""));
    }
}
