package test;

import org.junit.jupiter.api.Test;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupplierTest {
    /*
    4. Supplier [10 Marks]
    Write a lambda expression that returns a new StringBuilder containing the string "abc".
     */
    @Test
    public void d_supplier1() {
        Supplier<StringBuilder> sup = () -> new StringBuilder("abc");

        assertEquals("abc", sup.get().toString());
    }
}
