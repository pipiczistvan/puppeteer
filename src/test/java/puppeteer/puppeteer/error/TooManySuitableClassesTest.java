package puppeteer.puppeteer.error;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import puppeteer.annotation.premade.Wire;
import puppeteer.manager.AbstractManager;

public class TooManySuitableClassesTest extends ExceptionTestBase {

    @Wire
    private static AbstractManager managers;

    @Test
    public void tooManyClassTest() {
        Assert.assertThat(managers, CoreMatchers.nullValue());
    }

}
