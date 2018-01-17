package puppeteer.puppeteer.error;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import puppeteer.Puppeteer;
import puppeteer.exception.PuppeteerException;
import puppeteer.puppeteer.success.TestBase;

import java.net.URL;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class ExceptionTestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected static Puppeteer puppeteer;

    @Before
    public void init() {
        thrown.expect(PuppeteerException.class);

        URL successURL = TestBase.class.getClassLoader().getResource("puppeteer/puppeteer/success");
        URL errorURL = TestBase.class.getClassLoader().getResource("puppeteer/puppeteer/error");
        URL managerURL = TestBase.class.getClassLoader().getResource("puppeteer/manager");

        puppeteer = new Puppeteer(asList(successURL, errorURL, managerURL), emptyList(), asList("puppeteer.puppeteer.success", "puppeteer.manager"));


        puppeteer.useDefaultAnnotations();
        puppeteer.processAnnotations();
    }

}
