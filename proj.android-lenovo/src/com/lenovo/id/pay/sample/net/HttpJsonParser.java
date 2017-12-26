package com.lenovo.id.pay.sample.net;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;


public class HttpJsonParser {
	private static final String TAG = "HttpJsonParser";
	
	public static String parseError(HttpResponse response) {
		try	{
			String errorstring = EntityUtils.toString(response.getEntity());
			Log.e(TAG, "parseError:" + errorstring);
	        return parseError(errorstring);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static int parserIntError(HttpResponse response) {
		int ret = 0;
		if(response == null || response.getStatusLine() == null)
			return ret;
		ret = response.getStatusLine().getStatusCode();
	    if (ret != 200){
	    	String code = HttpJsonParser.parseError(response);
			if (code != null && (code.substring(0, 3).equalsIgnoreCase("USS"))) {
				ret = Integer.valueOf(code.substring(4));
			}
			Log.e(TAG, "parserIntError : ret = " + ret + ",response = " + code);
		} else {
			ret = 0;
		}
	    return ret;
	}
	public static String  parserOrderInfo(HttpResponse response) {
		String rtv ="";
		if(response == null || response.getStatusLine() == null)
			return rtv;
		int ret = response.getStatusLine().getStatusCode();
	    if (ret == 200){
			Log.e(TAG, "200 ");

			try {
				String resultstring = EntityUtils.toString(response.getEntity());
				JSONObject jb = new JSONObject(resultstring);
					if(!jb.isNull("orderId")) {
						rtv = jb.getString("orderId");
					}
				
			} catch (ParseException e) {
					e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
	    	
		} else {
			Log.e(TAG, "parserOrderInfo error"+ret);
		}
	    return rtv;
	}
	public static boolean  parserCheckOrderInfo(HttpResponse response) {
		boolean rtv = false;
		if(response == null || response.getStatusLine() == null)
			return rtv;
		int ret = response.getStatusLine().getStatusCode();
	    if (ret == 200){
		   rtv = true;
	    	
		} else {
			Log.e(TAG, "parserCheckOrderInfo error");
		}
	    return rtv;
	}

	private static String parseError(String errorstring) {
		try	{
	        JSONObject jb = new JSONObject(errorstring);
	        JSONObject error = jb.getJSONObject("Error"); 
	        String errorcode = error.optString("Code");
	        return errorcode;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	private HttpJsonParser() {}
}
