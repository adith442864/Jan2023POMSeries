package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.utils.AppConstants;
import com.qa.opencart.utils.ExcelUtil;

public class LoginPageNegativeTest extends BaseTest {
	
	@DataProvider
	public Object[][] incorrectLoginTestData() {
		
		return new Object[][] {
			{"auto123@gmail.com", "123456"},
			{"test@@gmail.com", "123456"},
			{"auto", "test"},
			{"#$#$#$@#@", "#$#$#@$#@4"},
		};
	}
	
	@DataProvider(name="incorrectExcelData")
	public Object[][] incorrectLoginTestDataFromExcel() {
		Object data[][] = ExcelUtil.getTestData(AppConstants.LOGIN_SHEET_NAME);
		return data;
	}
	
	@Test(dataProvider = "incorrectLoginTestData")
	public void loginWithWrongCredentialsTest(String userName, String password) {
		Assert.assertTrue(loginPage.doLoginwithWrongCredentials(userName, password));
		
	}
	
	@Test(dataProvider = "incorrectExcelData")
	public void loginWithWrongCredFromExcelTest(String userName, String password) {
		Assert.assertTrue(loginPage.doLoginwithWrongCredentials(userName, password));
		
	}
}

