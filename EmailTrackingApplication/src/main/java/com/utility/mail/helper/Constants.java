package com.utility.mail.helper;

public final class Constants {

	/**
	 * OTM related constants
	 */
	public static final String OTM_URL = "otm_url";
	public static final String OTM_AUTH = "otm_auth";
	public static final String OTM_USERNAME = "otm_username";
	public static final String OTM_PASSWORD = "otm_password";
	public static final String OTM_DBXML = "/GC3/glog.integration.servlet.DBXMLServlet?command=xmlExport";
	public static final String WMServlet = "/GC3/glog.integration.servlet.WMServlet";
	public static final String ERROR_CODE_401 = "HTTP response code: 401";
	public static final String ERROR_CODE_CONNECT_EXCEPTION = "java.net.ConnectException";
	/**
	 * Other constants
	 */
	public static final String HTTP = "HTTP";
	public static final String VALID_USER = "VALID_USER";
	public static final String INVALID_USER = "<AUTHENTICATION>INVALID_USER</AUTHENTICATION>";
	public static final String NETWORK_ERROR = "<NETWORK_ERROR>OTM SERVER NOT REACHABLE</NETWORK_ERROR>";

	/**
	 * Xsl files
	 */
	public static final String XSL_REMOVE_NS = "xsl/removeNS.xsl";
	public static final String XSL_CREATE_HTML = "xsl/emailContentXSL.xsl";
	public static final String XSL_SHIP_CREATE_HTML = "xsl/emailContentShipXSL.xsl";

}
