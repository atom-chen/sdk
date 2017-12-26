package com.lenovo.id.pay.sample.net;
  
import java.io.Reader;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


/**
 * HttpParser Toolkit Class
 * 
 * {@hide}
 */
public class HttpParser {

	private HttpParser() {}
	
	public static String parseError(HttpResponse response) {
		try	{
			Reader reader = new StringReader(EntityUtils.toString(response.getEntity()));
	        return parseError(reader);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static String parseError(Reader reader) {
		String value = null;
		try	{
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser xp = factory.newPullParser();
	        xp.setInput(reader);
	        int eventType = xp.getEventType();
	        while (eventType != XmlPullParser.END_DOCUMENT) {
	        	if(eventType == XmlPullParser.START_TAG) {
	        		if( xp.getName().equalsIgnoreCase("Code")) {
	        			value = xp.nextText();
	        		}
	        		if( xp.getName().equalsIgnoreCase("url")) {
	        			String temp = xp.nextText();
	        			if(temp != null && !"".equalsIgnoreCase(temp))
	        				value = value+"#"+temp;
	        		}
	        	} 
	        	
	        	eventType = xp.next();
	        }
	        return value;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}	
}
