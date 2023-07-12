package com.qa.opencart.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.AppConstants;

public class AccountsPageTest extends BaseTest {
	
//	WebDriver driver;
//	LoginPage loginPage;
//	AccountsPage accPage;
	
//	@BeforeTest
//	public void setup() {
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.manage().deleteAllCookies();
//		driver.get("https://naveenautomationlabs.com/opencart/index.php?route=account/login");
//		loginPage = new LoginPage(driver);
//		accPage = loginPage.doLogin("janautomation@gmail.com", "Selenium@12345");
		
//	}
	
	@BeforeClass
	public void accPageSetup() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test
	public void accPageTitleTest() {
		String actTitle = accPage.getAccPageTitle();
		Assert.assertEquals(actTitle, AppConstants.ACCOUNT_PAGE_TITLE_VALUE);
	}
	
	@Test
	public void isLogoutLinkExistTest() {
		Assert.assertTrue(accPage.isLogoutLinkExist());
	}
	
	@Test
	public void isMyAccountLinkExist() {
		Assert.assertTrue(accPage.isMyAccountLinkExist());
	}
	
	@Test
	public void accPageHeaderCountTest() {
		List<String> accAccHeadersList = accPage.getAccountPageHeadersList();
		Assert.assertEquals(accAccHeadersList.size(),4);
	}
	
	@Test
	public void accPageHeaderTest() {
		List<String> accAccHeadersList = accPage.getAccountPageHeadersList();
		//List<String> expAccHeadersList = Arrays.asList("My Account","My Orders","My Affiliate Account", "Newsletter");
		Assert.assertEquals(accAccHeadersList, AppConstants.EXP_ACCOUNTS_HEADERS_LIST);
	}
	
	
	
//	
//	@AfterTest
//	public void tearDown() {
//		driver.quit();
//	}
	
	

}
