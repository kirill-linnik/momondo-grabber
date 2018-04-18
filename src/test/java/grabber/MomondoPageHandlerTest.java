package grabber;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MomondoPageHandlerTest {

    @Test
    public void testNrParsing() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getPrice = MomondoPageHandler.class.getDeclaredMethod("getPrice", String.class);
        getPrice.setAccessible(true);

        int price = (int) getPrice.invoke(getPrice, "1 234&nbsp;EUR");
        assertEquals(1234, price);
    }

}