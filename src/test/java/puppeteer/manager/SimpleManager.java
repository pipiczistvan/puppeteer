package puppeteer.manager;

import puppeteer.annotation.premade.Component;

@Component
public class SimpleManager extends AbstractManager {

    @Override
    public void manage() {
        System.out.println("I'm SimpleManager");
    }

}
