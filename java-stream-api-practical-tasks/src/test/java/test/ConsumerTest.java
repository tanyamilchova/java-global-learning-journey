package test;

import org.junit.jupiter.api.Test;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsumerTest {
    /*
    3. Consumer [10 Marks]
    Write a lambda expression that appends the string "abc" to the given StringBuilder.
     */
    @Test
    void c_consumer1() {
        StringBuilder sb = new StringBuilder("xyz");
        Consumer<StringBuilder> appenderConsumer =
                str -> str.append("abc");
        appenderConsumer.accept(sb);

        assertEquals("xyzabc", sb.toString());
    }
}
