package puppeteer.puppeteer.error;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import puppeteer.annotation.premade.Wire;

public class NotFoundSuitableClassTest extends ExceptionTestBase {

    @Wire
    private static NotAnnonatatedInjectionTester notAnnonatatedInjectionTester;

    @Test
    public void notAnnotatedInjectionTest() {
        Assert.assertThat(notAnnonatatedInjectionTester, CoreMatchers.nullValue());
    }

}
