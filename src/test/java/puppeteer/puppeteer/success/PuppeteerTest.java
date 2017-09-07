package puppeteer.puppeteer.success;

import org.junit.Test;
import puppeteer.annotation.premade.Wire;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PuppeteerTest extends TestBase {

    @Wire
    private static ConstructorTester constructorTester;

    @Wire
    private static FieldTester fieldTester;

    @Wire
    private static SimpleInjectTester simpleInjectTester1;

    private SimpleInjectTester simpleInjectTester2;

    @Test
    public void startTest() {
        assertThat(simpleInjectTester1, notNullValue());
    }

    @Test
    public void getInstanceOfTest() {
        simpleInjectTester2 = puppeteer.getInstanceOf(SimpleInjectTester.class);
        assertThat(simpleInjectTester2, notNullValue());
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
        assertThat(fieldTester.puppeteer, notNullValue());
        assertEquals(fieldTester.puppeteer, puppeteer);
    }
}
