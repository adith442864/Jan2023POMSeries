package com.qa.opencart.utils;

import java.util.Arrays;
import java.util.List;

public class AppConstants {
	
	//LoginPage
	public static final String LOGIN_PAGE_TITLE_VALUE = "Account Login";
	public static final String LOGIN_PAGE_URL_FRACTION_VALUE = "route=account/login";
	
	//AccountsPage
	public static final String ACCOUNT_PAGE_TITLE_VALUE = "My Account";
	
	//TimeOuts
	public static final int SHORT_DEFAULT_WAIT = 5;
	public static final int MEDIUM_DEFAULT_WAIT = 10;
	public static final int LONG_DEFAULT_WAIT = 20;

	//headers section:
	public static final List<String> EXP_ACCOUNTS_HEADERS_LIST = 
			Arrays.asList("My Account","My Orders","My Affiliate Account", "Newsletter");
	public static final CharSequence LOGIN_ERROR_MESSAGE = "Warning: No match for E-Mail Address and/or Password.";
	public static final String USER_REG_SUCCESS_MESSG = "Your Account Has Been Created!";
	
	/******************************Sheet Names***************************/
	public static final String REGISTER_SHEET_NAME = "register";
	public static final String LOGIN_SHEET_NAME = "login";
	
}
