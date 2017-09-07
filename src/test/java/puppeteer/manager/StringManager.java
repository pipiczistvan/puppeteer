package puppeteer.manager;

import puppeteer.annotation.premade.Component;
import puppeteer.annotation.premade.Unique;

@Unique
@Component
public class StringManager extends AbstractManager<String> {

    @Override
    public void manage() {
        System.out.println("I'm StringManager");
    }

}
