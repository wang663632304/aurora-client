package it.vivido.aurora.client.http;

public class HttpConst {

	public static String DEFAULT_BASE_URL = "http://www.auroraproject.net/";
	
	public static String LOGIN_URL = "api/login?username=%s&password=%s";
	
	
	public static String BuildHTTPRequest(String url, Object ... args)
	{
		return String.format(DEFAULT_BASE_URL + url, args);
	}
}
