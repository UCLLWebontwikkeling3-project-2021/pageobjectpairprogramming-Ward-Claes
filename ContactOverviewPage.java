package TestenMetPageObjectPattern;
// Story05_pair2
// Herbert Gonzalez
// Ruben Bottu
// Ward Claes

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactOverviewPage extends Page{

    public ContactOverviewPage(WebDriver driver) {

        super(driver);
        this.driver.get(getPath()+"?action=contactOverview"); //TODO verandeer command naar action als nodig

    }

    public void goToPage(){
        this.driver.get(getPath()+"?action=contactOverview"); //TODO verandeer command naar action als nodig
    }

    public boolean containsContactWithName(String name) {

        List <WebElement> listItems= driver.findElements(By.cssSelector("table tr"));
        List<String> names = Arrays.asList(name.split(" "));
        boolean found = false;

        for (WebElement listItem:listItems) {
            String[] itemsList = listItem.getText().split(" ");
            ArrayList<String> items = new ArrayList(Arrays.asList(itemsList));
            if (items.containsAll(names)) {
                found = true;
            }
        }
        return found;
    }


    public boolean userHasContacts() {
        List<WebElement> listItems = this.driver.findElements(By.cssSelector("td"));
        return !listItems.isEmpty();

    }

    public boolean containsContactDate(String date) {

        List <WebElement> listItems= driver.findElements(By.cssSelector("table tr"));
        boolean found = false;
        for (WebElement listItem:listItems) {
            String[] itemsList = listItem.getText().split(" ");
            ArrayList<String> items = new ArrayList(Arrays.asList(itemsList));
            if (items.contains(date)) {
                found = true;
            }
        }
        return found;
    }

    public boolean containsContactTime(String time) {

        List <WebElement> listItems= driver.findElements(By.cssSelector("table tr"));
        boolean found = false;
        for (WebElement listItem:listItems) {
            String[] itemsList = listItem.getText().split(" ");
            ArrayList<String> items = new ArrayList(Arrays.asList(itemsList));
            if (items.contains(time)) {
                found = true;
            }
        }
        return found;
    }


}
