package com.qa.opencart.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.utils.AppConstants;
import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {
	
	private WebDriver driver; //default value is null
	private ElementUtil eleUtil;
	
	//1. const. of the page class
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(this.driver);
	}
	
	//2. private By locators:
	private By emailId = By.id("input-email");
	private By password = By.id("input-password");
	private By loginBtn = By.xpath("//input[@value='Login']");
	private By forgotPwdLink = By.linkText("Forgotten Password");
	private By footerLinks = By.xpath("//footer//a");
	private By loginErrorMessg = By.cssSelector(".alert.alert-danger.alert-dismissible");
	
	public static final Logger log = LogManager.getLogger(LoginPage.class);
	
	private By registerLink = By.linkText("Register");
	
	//3. public page actions/methods:
	@Step("getting login page title")
	public String getLoginPageTitle() {
		log.info("getting login page title");
		return eleUtil.waitForTitleAndCapture(AppConstants.LOGIN_PAGE_TITLE_VALUE, AppConstants.SHORT_DEFAULT_WAIT);
	}
	
	@Step("getting login page url")
	public String getLoginPageURL() {
		return eleUtil.waitForURLContainsAndCapture(AppConstants.LOGIN_PAGE_URL_FRACTION_VALUE, AppConstants.MEDIUM_DEFAULT_WAIT);
	}
	
	@Step("checking forgot pwd link exist on the login page")
	public boolean isForgotPwdLinkExist() {
		boolean flag = eleUtil.checkElementIsDisplayed(forgotPwdLink);
		return flag;
	}
	
	@Step("getting footer links.... ")
	public List<String> getFooterLinksList() {
		List<WebElement> footerLinksList = eleUtil.waitForElementsVisible(footerLinks, AppConstants.MEDIUM_DEFAULT_WAIT);
		List<String> footerTextList = new ArrayList<String>();
		for(WebElement e: footerLinksList) {
			String text = e.getText();
			footerTextList.add(text);
		}
		
		return footerTextList;
	}
	
	@Step("login with username {0} and password {1}")
	public AccountsPage doLogin(String userName,String pwd) {
//		driver.findElement(emailId).sendKeys(userName);
//		driver.findElement(password).sendKeys(pwd);
//		driver.findElement(loginBtn).click();
		System.out.println("correct creds are  :" +userName +" : " +pwd);
		eleUtil.waitForElementVisible(emailId, AppConstants.MEDIUM_DEFAULT_WAIT);
		eleUtil.doSendKeys(emailId, userName);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		//return the next landing page -- AccountPage -- page chaining model
		return new AccountsPage(driver);
		
	}
	
	@Step("login with wrong username {0} and password {1}")
	public boolean doLoginwithWrongCredentials(String userName,String pwd) {
		System.out.println("Wrong creds are  :" +userName +" : " +pwd);
		eleUtil.waitForElementVisible(emailId, AppConstants.MEDIUM_DEFAULT_WAIT);
		eleUtil.doSendKeys(emailId, userName);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		String errorMessg = eleUtil.doGetText(loginErrorMessg);
		System.out.println(errorMessg);
		if(errorMessg.contains(AppConstants.LOGIN_ERROR_MESSAGE)) {
			return true;
		}
		return false;
	}
	
	@Step("navigate to register page")
	public RegisterPage navigateToRegisterPage() {
		eleUtil.doClick(registerLink);
		return new RegisterPage(driver);
	}

}
