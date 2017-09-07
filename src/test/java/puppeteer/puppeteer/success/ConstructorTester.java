package puppeteer.puppeteer.success;

import puppeteer.Puppeteer;
import puppeteer.annotation.premade.Component;
import puppeteer.annotation.premade.Wire;
import puppeteer.manager.AbstractManager;

import java.util.List;

@Component
public class ConstructorTester {

    public List<AbstractManager> managers;

    public List<AbstractManager<Integer>> filteredManagers;

    public Puppeteer puppeteer;

    @Wire
    public ConstructorTester(List<AbstractManager> managers, List<AbstractManager<Integer>> filteredManagers, Puppeteer puppeteer) {
        this.managers = managers;
        this.filteredManagers = filteredManagers;
        this.puppeteer = puppeteer;
    }

}
