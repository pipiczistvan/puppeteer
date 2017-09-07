package puppeteer.puppeteer.success;

import puppeteer.Puppeteer;
import puppeteer.annotation.premade.Component;
import puppeteer.annotation.premade.Wire;
import puppeteer.manager.AbstractManager;
import puppeteer.manager.StringManager;

import java.util.List;

@Component
public class FieldTester {

    @Wire
    public List<AbstractManager> managers;

    @Wire
    public List<AbstractManager<Integer>> filteredManagers;

    @Wire
    public static StringManager stringManager1;

    @Wire
    public static StringManager stringManager2;

    @Wire
    public static Puppeteer puppeteer;

}
