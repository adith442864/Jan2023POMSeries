package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.frameworkexception.FrameworkException;

public class ElementUtil {
	
	private WebDriver driver; //123
	private JavaScriptUtil jsUtil;
	private final int DEFAULT_TIME_OUT=10;
	
	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		//wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		jsUtil = new JavaScriptUtil(this.driver);
	}
	
	
	public WebElement getElement(By locator, int timeOut) {
		WebElement element = waitForElementVisible(locator, timeOut);
		if(Boolean.parseBoolean(DriverFactory.highlightElement)) {
			jsUtil.flash(element);
		}
		return element;
	}
	
	/**
	 * These are custom methods used for creating, clicking and sendkeys web element.
	 * @param locator
	 * @return
	 */
	public WebElement getElement(By locator) {
		WebElement element = null;
		try{
			element = driver.findElement(locator);
			System.out.println("Element is found with locator: " +locator);
		}
		catch(NoSuchElementException e) {
			System.out.println("Element is not found using this locator..." +locator);
			element = waitForElementVisible(locator, DEFAULT_TIME_OUT);
			//element = driver.findElement(locator);
		}
		
		if(Boolean.parseBoolean(DriverFactory.highlightElement)) {
			jsUtil.flash(element);
		}
		
		return element;
	}
	
	public void doSendKeys(By locator,String value) {
		if(value==null) {
			System.out.println("null values are not allowed");
			throw new FrameworkException("VALUECANNOTBENULL");
		}
		doClear(locator);
		getElement(locator).sendKeys(value);
	}
	
	public void doClick(By locator) {
		getElement(locator).click();
	}
	
	public void doClick(By locator,int timeOut) {
		checkElementClickable(locator, timeOut).click();
	}
	
	public void doClear(By locator) {
		getElement(locator).clear();
	}
	
	public String doGetText(By locator) {
		return getElement(locator).getText();
	}

	public boolean checkElementIsDisplayed(By locator) {
		return getElement(locator).isDisplayed();
	}
	
	public String doGetAttributeValue(By locator,String attrName) {
		return getElement(locator).getAttribute(attrName);
	}
	
	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}
	
	public List<String> getElementsAttributeValue(By locator,String attrName) {
		List<WebElement> eleList = getElements(locator);
		ArrayList<String> eleAttrList = new ArrayList<String>();
		
		for(WebElement e: eleList) {
			String attrValue = e.getAttribute(attrName);
			//System.out.println(attrValue);
			eleAttrList.add(attrValue);
		}
		return eleAttrList;
	}
	
	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}
	
	public List<String> getElementsTextList(By locator) {
		List<WebElement> elementsLinksList = getElements(locator);
		List<String> elesTextList = new ArrayList<String>();
		for(WebElement e: elementsLinksList) {
			String text = e.getText();
			//System.out.println(text);
			elesTextList.add(text);
		}
		return elesTextList;
	}
	
	public void clickElementFromPageSection(By locator,String eleText) {
		List<WebElement> eleList = getElements(locator);
		for(WebElement e: eleList) {
			String text = e.getText();
			if(text.equals(eleText)) {
				e.click();
				break;
			}
		}
	}
	
	public void search(String searchKey, By searchLocator,String suggName, By locator) 
			throws InterruptedException {
		
		doSendKeys(searchLocator, searchKey);
		Thread.sleep(3000);
		
		List<WebElement> suggList = getElements(locator);
		System.out.println("Total suggestions: " +suggList.size());
		
		if(suggList.size() > 0) {
			for(WebElement e: suggList) {
				String text = e.getText();
				if(text.length()>0) {
					System.out.println(text);
					if(text.contains(suggName)) {
						e.click();
						break;
					}
				}
				else {
					System.out.println("blank values -- no suggestions");
					break;
				}
				
			}
		}
		else {
			System.out.println("no search suggestions found...");
		}
		
	}
	
	public boolean isElementDisplayed(By locator) {
		List<WebElement> eleList = getElements(locator);
		if(eleList.size() > 0) {
			System.out.println(locator+" element is present on the page");
			return true;
		}else {
			return false;
		}
	}
	
	//****************************Drop Down Utils***************************/
	
	
	public void doSelectDropDownByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		select.selectByIndex(index);
	}
	
	public void doSelectDropDownByVisibleText(By locator, String text) {
		Select select = new Select(getElement(locator));
		select.selectByVisibleText(text);
	}
	
	public void doSelectDropDownByValueAttribute(By locator, String value) {
		Select select = new Select(getElement(locator));
		select.selectByValue(value);
	}
	
	public int getDropDownValueCount(By locator) {
		return getAllDropDownOptions(locator).size();
	}
	
	public List<String> getAllDropDownOptions(By locator) {
		Select select = new Select(getElement(locator));
		List<WebElement> optionsList =  select.getOptions();
		List<String> optionsValueList = new ArrayList<String>();
		System.out.println("total values: "+optionsList.size());
		
		for(WebElement e: optionsList) {
			String text = e.getText();
			System.out.println(text);
			optionsValueList.add(text);
		}
		
		return optionsValueList;
	}
	
	public boolean doSelectDropDownValue(By locator,String dropDownValue) {
		boolean flag = false;
		Select select = new Select(getElement(locator));
		List<WebElement> optionsList =  select.getOptions();
		System.out.println("total values: "+optionsList.size());
		
		for(WebElement e: optionsList) {
			String text = e.getText();
			System.out.println(text);
			if(text.equals(dropDownValue)) {
				flag=true;
				e.click();
				break;
			}
//			else {
//				System.out.println(dropDownValue+"is not present in the drop down" +locator);
//			}
		}
		
		if(flag == false) {
			System.out.println(dropDownValue+"is not present in the drop down" +locator);
		}
		
		return flag;
	}
	
	public boolean doSelectValueFromDropDownWithoutSelect(By locator,String value) {
		boolean flag = false;
		List<WebElement> optionsList = getElements(locator);
		for(WebElement e: optionsList) {
			String text = e.getText();
			if(text.equals(value)) {
				flag = true;
				e.click();
				break;
			}
		}
		
		if(flag == false) {
			System.out.println(value+ " is not present in the drop down" +locator);
		}
		
		return flag;
	}
	
	public void selectChoice(By locator, String... value) {
		
		List<WebElement> optionsList = getElements(locator);
		
		System.out.println(optionsList.size());
		boolean flag = false;
		if(!value[0].equalsIgnoreCase("all")) {
			for(WebElement e: optionsList) {
				String text = e.getText();
				System.out.println(text);
				
				//multiple selection logic:
				for(int i=0; i<value.length; i++) {
					if(text.equals(value[i])) {
						flag = true;
						e.click();
						break;
					}
				}
			}
	    }
		else {
	    	//all selection logic:
			for(WebElement e: optionsList) {
				String text = e.getText();
				if(!text.equals("−")) {
					flag = true;
					e.click();
					
				}
			}
	    }
		
		if(flag == false) {
			System.out.println("choice is not available");
		}
			
	}
	
	//****************************Actions Class Utils***************************/
	
	public void doActionsSendKeys(By locator, String value) {
		Actions act = new Actions(driver);
		act.sendKeys(getElement(locator),value).build().perform();
	}
	
	public void doActionsClick(By locator) {
		Actions act = new Actions(driver);
		act.click(getElement(locator)).build().perform();
	}
	
	public void doActionsClick(By locator, int timeOut) {
		Actions act = new Actions(driver);
		//act.click(getElement(locator)).build().perform();
		act.click(checkElementClickable(locator, timeOut)).build().perform();
	}
	
	public void doDragAndDrop(By sourceLocator, By targetLocator) {
		Actions act = new Actions(driver);
		act.dragAndDrop(getElement(sourceLocator), getElement(targetLocator)).build().perform();
	}
	
	public void doContextClick(By locator) {
		Actions act = new Actions(driver);
		act.contextClick(getElement(locator)).build().perform();
	}
	
	public void doMoveToElement(By locator) {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(locator)).build().perform();
	}
	
	public void handleTwoLevelMenu(By parentMenu, By childMenu) throws InterruptedException {
		//WebElement parentMenuElement = getElement(parentMenu);
		//Actions act = new Actions(driver);
		//act.moveToElement(parentMenuElement).build().perform();
		doMoveToElement(parentMenu);
		Thread.sleep(2000);
		doClick(childMenu);
	}
	
	public void handleTwoLevelMenu(By parentMenu, String childMenuLinkText) throws InterruptedException {
		WebElement parentMenuElement = getElement(parentMenu);
		//Actions act = new Actions(driver);
		//act.moveToElement(parentMenuElement).build().perform();
		doMoveToElement(parentMenu);
		Thread.sleep(3000);
		doClick(By.linkText(childMenuLinkText));
	}
	
	
	public void multiLevelMenuChildmenuHandle(By parentMenuLocator, String level2LinkText, String level3LinkText, 
			String level4LinkText) throws InterruptedException {
		
		WebElement level1 = getElement(parentMenuLocator);
		
		Actions act = new Actions(driver);
		act.moveToElement(level1).build().perform();
		Thread.sleep(2000);
		
		WebElement level2 = getElement(By.linkText(level2LinkText));
		act.moveToElement(level2).build().perform();
		Thread.sleep(2000);
		
		WebElement level3 = getElement(By.linkText(level3LinkText));
		act.moveToElement(level3).build().perform();
		Thread.sleep(2000);
		
		//driver.findElement(By.linkText(level4LinkText)).click();
		doClick(By.linkText(level4LinkText));
		
	}
	
	//****************************Wait Utils***************************//
	
	public Alert waitForAlertJSPopUpWithFluentWait(int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.ignoring(NoAlertPresentException.class)
				.pollingEvery(Duration.ofSeconds(pollingTime))
				.withMessage("-----time out is done..element is not found....");
				
		return wait.until(ExpectedConditions.alertIsPresent());
	}
	
	
	public Alert waitForAlertJsPopUp(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.alertIsPresent());
		
	}
	
	public String alertJSGetText(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		 return waitForAlertJsPopUp(timeOut).getText();
		
	}
	
	public void alertAccept(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		waitForAlertJsPopUp(timeOut).accept();
		
	}
	
	public void alertDismiss(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		waitForAlertJsPopUp(timeOut).dismiss();
		
	}
	
	public void enterAlertValue(int timeOut,String value) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		waitForAlertJsPopUp(timeOut).sendKeys(value);
		
	}
	
	
	
	public String waitForTitleAndCapture(String titleFraction, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if(wait.until(ExpectedConditions.titleContains(titleFraction))) {
			String title = driver.getTitle();
			return title;
		}else {
			System.out.println("title is not present within the given timeout: " +timeOut);
			return null;
		}
	}
	
	public String waitForFullTitleAndCapture(String titleVal, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if(wait.until(ExpectedConditions.titleIs(titleVal))) {
			String title = driver.getTitle();
			return title;
		}else {
			System.out.println("title is not present within the given timeout: " +timeOut);
			return null;
		}
	}
	
	//url with wait:
	public String waitForURLContainsAndCapture(String urlFraction, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if(wait.until(ExpectedConditions.urlContains(urlFraction))) {
			String url = driver.getCurrentUrl();
			return url;
		}else {
			System.out.println("url is not present within the given timeout: " +timeOut);
			return null;
		}
	}
	
	public String waitForURLToBe(String urlValue, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if(wait.until(ExpectedConditions.urlToBe(urlValue))) {
			String url = driver.getCurrentUrl();
			return url;
		}else {
			System.out.println("url is not present within the given timeout: " +timeOut);
			return null;
		}
	}
	
	//windows:
	public boolean waitForTotalWindows(int totalWindowsToBe, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.numberOfWindowsToBe(totalWindowsToBe));
	}
	
	//frames with wait:
	public void waitForFrameAndSwitchToItByIdOrName(int timeOut, String idOrName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(idOrName));
	}
	
	public void waitForFrameAndSwitchToItByIndex(int timeOut, String frameIndex) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}
	
	public void waitForFrameAndSwitchToItByFrameElement(int timeOut, String frameElement) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}
	
	public void waitForFrameAndSwitchToItByLocator(int timeOut, By frameLocator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}
	
	public void waitForFrameAndSwitchToItWithFluentWait(int timeOut, int pollingTime,String idOrName) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.ignoring(NoSuchFrameException.class)
				.pollingEvery(Duration.ofSeconds(pollingTime))
				.withMessage("-----time out is done..Frame is not found....");
				
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(idOrName));
	}
	
	/**
	 * An expectation for checking that an element is present on the DOM of a page. 
	 * This does not necessarily mean that the element is visible.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForElementPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	/**
	 * An expectation for checking that there is at least one element present on a web page.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForElementsPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	
	/**
	 * An expectation for checking that an element is present on the DOM of a page and visible on the page. 
	 * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForElementVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		WebElement element =  wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		if(Boolean.parseBoolean(DriverFactory.highlightElement)) {
			jsUtil.flash(element);
		}
		return element;
		
	}
	
	/**
	 * An expectation for checking that all elements present on the web page that match the locator are visible.
	 * Visibility means that the elements are not only displayed but also have a height and width that is greater than 0.
	 * default timeout = 500 milli seconds
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForElementsVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}
	
	//default timeout = intervalTime
	public List<WebElement> waitForElementsVisible(By locator, int timeOut, int intervalTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut), Duration.ofSeconds(intervalTime));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}
	
	/**
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public void clickElementWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	/**
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement checkElementClickable(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		WebElement element =  wait.until(ExpectedConditions.elementToBeClickable(locator));
		if(Boolean.parseBoolean(DriverFactory.highlightElement)) {
			jsUtil.flash(element);
		}
		return element;
	}
	
	public WebElement waitForElementVisibleWithFluentWait(By locator, int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(ElementNotInteractableException.class)
				.pollingEvery(Duration.ofSeconds(pollingTime))
				.withMessage("-----time out is done..element is not found....");
				
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		if(Boolean.parseBoolean(DriverFactory.highlightElement)) {
			jsUtil.flash(element);
		}
		
		return element;
	}
	
	public WebElement waitForElementPresenceWithFluentWait(By locator, int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(timeOut))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.ignoring(ElementNotInteractableException.class)
				.pollingEvery(Duration.ofSeconds(pollingTime))
				.withMessage("-----time out is done..element is not found....");
				
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	/************************ Cutom Wait************************/
	public WebElement retryingElement(By locator, int timeOut) {

		WebElement element = null;
		int attempts = 0;
		
		while (attempts < timeOut) {
			try {
				element = driver.findElement(locator);
				System.out.println("element is found in attempt : " + attempts + " for : " + locator);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found in attempt : " + attempts + " for : " + locator);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			attempts++;
		}

		if (element == null) {
			try {
				throw new Exception("ELEMENTNOTFOUNDEXCEPTION");
			} catch (Exception e) {
				System.out.println("element is not found exception....tried for : " + timeOut + " secs"
						+ " with the interval of 500 milliseconds ");
			}
		}
		return element;

	}

	public WebElement retryingElement(By locator, int timeOut, int pollingTime) {

		WebElement element = null;
		int attempts = 0;
		
		while (attempts < timeOut) {
			try {
				element = driver.findElement(locator);
				System.out.println("element is found in attempt : " + attempts + " for : " + locator);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found in attempt : " + attempts + " for : " + locator);
				try {
					Thread.sleep(pollingTime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			attempts++;
		}

		if (element == null) {
			try {
				throw new Exception("ELEMENTNOTFOUNDEXCEPTION");
			} catch (Exception e) {
				System.out.println("element is not found exception....tried for : " + timeOut + " secs"
						+ " with the interval of : " + pollingTime + " ms ");
			}
		}

		return element;

	}
	
	/**
	 * This method is used to wait for page to be loaded
	 * @param timeOut
	 */
	public boolean isPageLoaded() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		String flag = 
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == \'complete\'"))
			.toString(); //"true" or "false"
		return Boolean.parseBoolean(flag);
	}
	
	public void waitForPageLoad(int timeOut) {
		long endTime = System.currentTimeMillis() + timeOut;
		while(System.currentTimeMillis() < endTime) {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			String pageState = js.executeScript("return document.readyState").toString();
			if(pageState.equals("complete")) {
				System.out.println("DOM PAGE is fully loaded now...");
			}else {
				System.out.println("DOM Page is not loaded... " + pageState);
			}
		}
	}



}
