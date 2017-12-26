package com.lenovo.id.pay.sample.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;


public class HttpUtil {
	private static final String TAG = "HttpUtil";
	
	public static  HttpResponse request(RequestMethod method, String host, String path, 
			String[] params, Map<String, String> headers) throws HttpUtilException {
		 
	    HttpResponse resp = null;
	    Log.e(TAG, "demo accessing server url = " + host + path);
	    resp = streamRequest(method, host, path, params, headers).response;
	    return resp;
	}
	 
	public enum RequestMethod {
	    GET, POST, PUT;
	}
		
	public enum ERRTYPE {
	    DIGIT, STRING, STRUCT;
	}
	
	private static final class RequestAndResponse {
	    /** The request */
	    //public final HttpUriRequest request;
	    /** The response */
	    public final HttpResponse response;

	    protected RequestAndResponse(HttpUriRequest request, HttpResponse response) {
	        //this.request = request;
	        this.response = response;
	    }
	}
	 
	public static int handleStatusCodeRetInt(HttpResponse response){
	    int ret = response.getStatusLine().getStatusCode();
	    if (ret != 200){
	    	String code = HttpParser.parseError(response);
			if (code != null &&(code.substring(0,3).equalsIgnoreCase("USS"))) {
				ret = Integer.valueOf(code.substring(4));
			}
		} else {
			ret = 0;
		}
	    Log.e(TAG, "handleStatusCodeRetInt : " + ret);
	    return ret;
	}
	
	public static String handleErrorRetStr(HttpResponse response){
		String retData = null;
		int ret = 0;
		ret = response.getStatusLine().getStatusCode();
       	String code = HttpParser.parseError(response);
		if (code != null &&(code.substring(0,3).equalsIgnoreCase("USS"))) {
			retData = code;
		}else {
			retData =  "USS-0" + ret;
		}
		Log.e(TAG, "handleErrorRetStr : " + retData);
		return retData;
	}
	
	
    private static RequestAndResponse streamRequest(RequestMethod method,
	    String host, String path,  String params[],
	        Map<String, String> headers) throws HttpUtilException {
	    HttpRequestBase req = null;
	    String target = null;
	    if (method == RequestMethod.GET) {
	        target = buildURL(host, path, params);
	        req = createRequest(HttpHelper.HttpMethod.GET_METHOD, target);
	    } else {
	        target = buildURL(host, path, null);
	        req = createRequest(HttpHelper.HttpMethod.POST_METHOD, target);
	        setRequestEntity(req, params);
        }

		HttpResponse resp = null;
		try {
		    resp = executeRequest(req, headers);
		}catch (HttpUtilException e){
			
		}
		return new RequestAndResponse(req, resp);
    }
    
    private static HttpRequestBase createRequest(HttpHelper.HttpMethod method, 
	    String url) throws HttpUtilException {
		HttpRequestBase request = null;
		request = HttpHelper.getRequest(method, url);
		if (request == null)
		{
		    throw new HttpUtilException("create request error");
		}
		return request;
	}
    
    
    private static void setRequestEntity(HttpRequestBase request,
			String params[]) throws HttpUtilException {
		HttpHelper.setRequestEntity(request, params);
	}
		
    private static HttpResponse executeRequest(HttpRequestBase request, 
			Map<String, String> headers) throws HttpUtilException {
	
		//boolean flag = false;
		HttpResponse response = null;
		//do {
		    try{
				response = HttpHelper.executeHttpRequest(request, headers);
				if (response == null){
					throw new HttpUtilException("httpclient error return null", 1001);
				}
				return response;
		    }catch (ClientProtocolException e) {
			    throw new HttpUtilException("ClientProtocolException");
			} catch (IOException e) {
			    throw new HttpUtilException(e.getMessage());
			}
	    //}while (flag);
    }
	 /**
     * URL encodes an array of parameters into a query string.
     */
    public static String urlencode(String[] params) {
	     if (params.length % 2 != 0) {
	         throw new IllegalArgumentException("Params must have an even number of elements.");
	     }
         String result = "";
	     try {
	         boolean firstTime = true;
	         for (int i = 0; i < params.length; i += 2) {
	             if (params[i + 1] != null) {
	                 if (firstTime) {
	                     firstTime = false;
	                 } else {
	                     result += "&";
	                 }
	                 result += URLEncoder.encode(params[i], "UTF-8") + "="
	                          + URLEncoder.encode(params[i + 1], "UTF-8");
	             }
	         }
	         result = result.replace("*", "%2A");
	    } catch (Exception e) {
	        return null;
	    }
	    return result;
    }
	    
	private static String buildURL(String host, String target, String[] params) {
//		if (!target.startsWith("/")) {
//	        target = "/" + target;
//	    }
        try {
            // We have to encode the whole line, then remove + and / encoding
            // to get a good OAuth URL.
            //target = URLEncoder.encode(target, "UTF-8");
	        //target = target.replace("%2F", "/");
	        //target = target.replace("%3F", "?");
            if (params != null && params.length > 0) {
                target += "?" + urlencode(params);
            }
            // These substitutions must be made to keep OAuth happy.
            target = target.replace("+", "%20").replace("*", "%2A");
       } catch (Exception e) {
            return null;
       }
       return host + target;
    }
	
	private HttpUtil() {}
}
