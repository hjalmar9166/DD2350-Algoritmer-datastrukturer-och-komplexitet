import org.junit.Test;
import org.junit.Before;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class PersistentArrayTest {
    PersistentArray array;

    @Before
    public void setUp() {
        array = new PersistentArray();
    }

    @Test
    public void lengthIsZeroWhenInitialised() {
        assertThat(array.size(), equalTo(0));
    }

    @Test
    public void lengthAfterMultipleSetsIsCorrect() {
        int numberOfElements = 10;
        for (int i = 0; i < numberOfElements; i++) {
            array.set(i, i);
        }
        assertThat(array.size(), equalTo(numberOfElements));
    }

    @Test
    public void getAfterSetAtSameIndexReturnsCorrectValue() {
        int setValue = 10;
        array.set(1, setValue);
        int getValue = array.get(1);
        assertThat(getValue, equalTo(setValue));
    }

    @Test
    public void unsetRevertsToPreviousVersion() {
        int setOld = 10;
        int setNew = 20;
        array.set(1, setOld);
        array.set(1, setNew);
        array.unset();
        assertThat(array.get(1), equalTo(setOld));
    }

    @Test
    public void getReturnsZeroWhenElementDoesNotExist() {
        array.set(1, 10);
        assertThat(array.get(2), equalTo(0));
    }

    @Test
    public void getAfterArrayIsExpandedIsCorrect() {
        array.set(1, 10);
        int expectedValue = array.get(1);
        array.set(13, 20);
        assertThat(array.get(1), equalTo(expectedValue));
    }
}