package TestenMetPageObjectPattern;
// Story05_pair2
// Herbert Gonzalez
// Ruben Bottu
// Ward Claes
import domain.db.ContactDB;
import domain.db.ContactDBSQL;
import domain.db.PersonDBSQL;
import domain.model.Contact;
import domain.model.Person;
import domain.service.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import util.DbConnectionService;
import util.Encryption;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class ContactOverviewTest {
	private WebDriver driver;
	private String path = "http://localhost:8080/Controller";
	private ContactOverviewPage contactOverviewPage;
	private HomePage homePage;
	private PersonDBSQL personDBSQL;
	private ContactDB contactDB;

	private PersonService personService;
	@Before
	public void setUp() {
		//System.setProperty("webdriver.chrome.driver", "/Users/.../web3pers/chromedriver");
			// windows: gebruik dubbele \\ om pad aan te geven
			// hint: zoek een werkende test op van web 2 ...
		System.setProperty("webdriver.chrome.driver", "D:\\Desktop\\ucll\\1-2\\Web2\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
//		driver.get(path+"?command=SignUp");
		DbConnectionService.connect("jdbc:postgresql://databanken.ucll.be:62021/2TX33","web3_project_r0715727_ward_claes");

		contactOverviewPage = new ContactOverviewPage(driver);
		homePage = new HomePage(driver);
		personService = new PersonService(); ///TODO verandeer dit met u eigen service
		personDBSQL = new PersonDBSQL();
		contactDB = new ContactDBSQL();

	}

	//TODO Pas dit aan naar eigen person classes
	public Person setUpTestPerson(String userId, String email , String pass, String firstName, String lastName) {
		Person person = new Person(userId,email,pass,firstName,lastName);
		personDBSQL.add(person);

		return personDBSQL.get(person.getUserid());
	}

	public void setUpTestContact( String contactFirstName,String contactLastName,String contactEmail,Person person){
		Contact contact = new Contact(contactFirstName,contactLastName,"2020-11-11 18:30:00","3211223344",contactEmail);
		contactDB.add(person.getUserid(),contact);
	}
	
	@After
	public void clean() {
	    driver.quit();
	}


	@Test
	public void ContactOverviewOfPersonWithoutContactShowsNoContacts() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		Person testPerson = setUpTestPerson(generateRandomUseridInOrderToRunTestMoreThanOnce("test"),"test@test.test", Encryption.encrypt("test"),"t","est");
		homePage.signIn(testPerson.getUserid(),"test");
		contactOverviewPage.goToPage();
		assertFalse(contactOverviewPage.userHasContacts());


	}
// initialiseren we testPerson?

	@Test
	public void ContactOverviewOfPersonWith1ContactShows1Contacts() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		Person testPerson = setUpTestPerson(generateRandomUseridInOrderToRunTestMoreThanOnce("test"),"test@test.test",Encryption.encrypt("test"),"t","est");
		setUpTestContact("contact","contact","contact@contact.contact",testPerson);
		homePage.signIn(testPerson.getUserid(),"test");
		contactOverviewPage.goToPage();
		assertTrue(contactOverviewPage.userHasContacts());
		assertTrue(contactOverviewPage.containsContactWithName("contact contact"));
		assertTrue(contactOverviewPage.containsContactDate("11/NOVEMBER/2020"));
		assertTrue(contactOverviewPage.containsContactTime("18:30"));
	}


	@Test
	public void ContactOverviewOfPersonWithMultipleContactsShowsAllContacts() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		Person testPerson = setUpTestPerson(generateRandomUseridInOrderToRunTestMoreThanOnce("test"),"test@test.test",Encryption.encrypt("test"),"t","est");
		setUpTestContact("contact","contact","contact@contact.contact",testPerson);
		setUpTestContact("contact2","contact2","contact@contact.contact",testPerson);
		homePage.signIn(testPerson.getUserid(),"test");
		contactOverviewPage.goToPage();
		assertTrue(contactOverviewPage.userHasContacts());
		assertTrue(contactOverviewPage.containsContactWithName("contact contact"));
		assertTrue(contactOverviewPage.containsContactWithName("contact2 contact2"));
		assertTrue(contactOverviewPage.containsContactDate("11/NOVEMBER/2020"));
		assertTrue(contactOverviewPage.containsContactTime("18:30"));

	}


/*	@Test
	public void test_Contact_OverviewShowsContact() {



		//TODO sign up

		//Via pagina
		//driver.get(path+"?command=SignUp");
		String useridRandom = generateRandomUseridInOrderToRunTestMoreThanOnce("jakke");
		//submitForm(useridRandom, "Jan", "Janssens", "jan.janssens@hotmail.com" , "1234");
//		setUpTestPerson("jan.janssens@hotmail.com");


		//TODO add new contact

		//Via pagina
		//driver.get(path+"?command=AddContact");
		//submitForm(useridRandom, "Ruben", "Bottu", "ruben.bottu@hotmail.com" , "1234");


//		//WhiteboxTesting via model

//
		//Via pageObject     maybe??
		//contactPage = new ContactPage(driver);
		//contactPage.add(something)??


		//Todo


		contactOverviewPage = new ContactOverviewPage(driver);

		String title = driver.getTitle();
		assertEquals("Overview",title);

		assertTrue(contactOverviewPage.containsContactWithName("Ruben"));
	}*/


	private String generateRandomUseridInOrderToRunTestMoreThanOnce(String component) {
		int random = (int)(Math.random() * 1000 + 1);
		return random+component;
	}

	private void fillOutField(String name,String value) {
		WebElement field=driver.findElement(By.id(name));
		field.clear();
		field.sendKeys(value);
	}

	private void submitForm(String userid, String firstName,String lastName, String email, String password) {
		fillOutField("userid", userid);
		fillOutField("firstName", firstName);
		fillOutField("lastName",lastName);
		fillOutField("email", email);
		fillOutField("password", password);

		WebElement button=driver.findElement(By.id("signUp"));
		button.click();
	}

}
