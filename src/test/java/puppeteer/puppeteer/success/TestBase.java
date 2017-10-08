package puppeteer.puppeteer.success;

import org.junit.BeforeClass;
import puppeteer.Puppeteer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public abstract class TestBase {

    protected static Puppeteer puppeteer;

    @BeforeClass
    public static void init() {
        puppeteer = new Puppeteer(singletonList("^.*/target/.*$"), asList("puppeteer.puppeteer.success", "puppeteer.manager"));

        puppeteer.useDefaultAnnotations();
        puppeteer.processAnnotations();
    }

}
