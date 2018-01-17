package puppeteer.puppeteer.success;

import org.junit.BeforeClass;
import puppeteer.Puppeteer;

import java.net.URL;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class TestBase {

    protected static Puppeteer puppeteer;

    @BeforeClass
    public static void init() {
        URL successURL = TestBase.class.getClassLoader().getResource("puppeteer/puppeteer/success");
        URL managerURL = TestBase.class.getClassLoader().getResource("puppeteer/manager");

        puppeteer = new Puppeteer(asList(successURL, managerURL), emptyList(), asList("puppeteer.puppeteer.success", "puppeteer.manager"));

        puppeteer.useDefaultAnnotations();
        puppeteer.processAnnotations();
    }

}
