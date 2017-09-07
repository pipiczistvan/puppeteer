package puppeteer.puppeteer.error;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import puppeteer.Puppeteer;
import puppeteer.exception.PuppeteerException;

public abstract class ExceptionTestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected static Puppeteer puppeteer;

    @Before
    public void init() {
        thrown.expect(PuppeteerException.class);

        puppeteer = new Puppeteer("puppeteer", "puppeteer.*");

        puppeteer.useDefaultAnnotations();
        puppeteer.processAnnotations();
    }

}
