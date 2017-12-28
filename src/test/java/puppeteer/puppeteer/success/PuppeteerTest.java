package puppeteer.puppeteer.success;

import org.junit.Test;
import puppeteer.annotation.premade.Wire;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class PuppeteerTest extends TestBase {

    @Wire
    private static ConstructorTester constructorTester;

    @Wire
    private static FieldTester fieldTester;

    @Wire
    private static SimpleInjectTester simpleInjectTester1;

    @Test
    public void startTest() {
        assertThat(simpleInjectTester1, notNullValue());
    }

    @Test
    public void getInstanceOfTest() {
        SimpleInjectTester explicitInstance1 = puppeteer.getInstanceOf(SimpleInjectTester.class);
        SimpleInjectTester explicitInstance2 = puppeteer.getInstanceOf(SimpleInjectTester.class);

        assertThat(explicitInstance1, notNullValue());
        assertThat(explicitInstance2, notNullValue());
        assertEquals(explicitInstance1, explicitInstance2);
    }

    @Test
    public void getNewInstanceOfTest() {
        SimpleInjectTester explicitInstance1 = puppeteer.getNewInstanceOf(SimpleInjectTester.class);
        SimpleInjectTester explicitInstance2 = puppeteer.getNewInstanceOf(SimpleInjectTester.class);

        assertThat(explicitInstance1, notNullValue());
        assertThat(explicitInstance2, notNullValue());
        assertNotEquals(explicitInstance1, explicitInstance2);
    }

    @Test
    public void constructorTest() {
        assertThat(constructorTester, notNullValue());
        assertThat(constructorTester.managers, notNullValue());
        assertThat(constructorTester.managers.size(), is(3));
        assertThat(constructorTester.filteredManagers, notNullValue());
        assertThat(constructorTester.filteredManagers.size(), is(1));
        assertThat(constructorTester.puppeteer, notNullValue());
    }

    @Test
    public void fieldTest() {
        assertThat(fieldTester, notNullValue());
        assertThat(fieldTester.managers, notNullValue());
        assertThat(fieldTester.managers.size(), is(3));
        assertThat(fieldTester.filteredManagers, notNullValue());
        assertThat(fieldTester.filteredManagers.size(), is(1));
        assertThat(FieldTester.stringManager1, not(equalTo(FieldTester.stringManager2)));
        assertThat(FieldTester.puppeteer, notNullValue());
        assertEquals(FieldTester.puppeteer, puppeteer);
    }
}
