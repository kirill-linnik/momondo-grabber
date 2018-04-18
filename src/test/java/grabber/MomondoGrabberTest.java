package grabber;

import grabber.repository.model.DestinationOption;
import grabber.repository.model.DestinationSetup;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MomondoGrabberTest {

    @Test
    public void testDestinationGeneration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getDestinationOptions = MomondoGrabber.class.getDeclaredMethod("getDestinationOptions", DestinationSetup.class);
        getDestinationOptions.setAccessible(true);

        DestinationSetup destinationSetup = new DestinationSetup();
        destinationSetup.setFromCity("Tallinn");
        destinationSetup.setToCity("Mallorca");
        destinationSetup.setFromDate(LocalDate.now());
        destinationSetup.setToDate(LocalDate.now().plusDays(9));
        destinationSetup.setMinDuration(7);

        Set<DestinationOption> destinationOptions = (Set<DestinationOption>) getDestinationOptions.invoke(getDestinationOptions, destinationSetup);
        assertEquals(3, destinationOptions.size());
    }

}