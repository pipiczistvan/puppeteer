package puppeteer.manager;

import puppeteer.annotation.premade.Component;

@Component
public class IntegerManager extends AbstractManager<Integer> {

    @Override
    public void manage() {
        System.out.println("I'm IntegerManager");
    }

}
