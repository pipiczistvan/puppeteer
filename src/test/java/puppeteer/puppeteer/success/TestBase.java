package puppeteer.puppeteer.success;

import org.junit.BeforeClass;
import puppeteer.Puppeteer;

public abstract class TestBase {

    protected static Puppeteer puppeteer;

    @BeforeClass
    public static void init() {
        puppeteer = new Puppeteer("puppeteer", "puppeteer.puppeteer.success", "puppeteer.manager");

        puppeteer.useDefaultAnnotations();
        puppeteer.processAnnotations();
    }

}
