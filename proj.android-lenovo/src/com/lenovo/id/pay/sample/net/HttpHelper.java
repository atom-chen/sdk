package com.lenovo.id.pay.sample.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;



public final class HttpHelper {
	
	public enum HttpMethod {
		GET_METHOD,
		PUT_METHOD,
		POST_METHOD,
		DELETE_METHOD
	}
	
	private HttpHelper() {	
	}

	public static HttpResponse executeHttpRequest(HttpMethod method, String url,
			Map<String, String> headers, String params[]) throws ClientProtocolException, IOException {
		
		HttpRequestBase request = getRequest(method, url);
		if (method == HttpMethod.POST_METHOD)
		{
			setRequestEntity(request, params);
		}
		return executeHttpRequest(request,headers);
	}
	
	public static HttpResponse executeHttpRequest(HttpRequestBase request,Map<String, String> headers) throws ClientProtocolException, IOException {
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				if (!request.containsHeader(key)) {
					request.setHeader(key, headers.get(key));
				}
			}
		}
		HttpClient client = HttpClientFactory.createClient();
		if(client == null) {
			return null;
		}
		return client.execute(request);
	}
	
	
	public static void setRequestEntity(HttpRequestBase request, String params[]) {
		//assert(params != null);
		//assert(params.length >= 2);
        if (params != null && params.length >= 2) {
            if (params.length % 2 != 0) {
                throw new IllegalArgumentException("Params must have an even number of elements.");
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            for (int i = 0; i < params.length; i += 2) {
                if (params[i + 1] != null) {
                    nvps.add(new BasicNameValuePair(params[i], params[i + 1]));
                }
            }

            try {
            	HttpEntityEnclosingRequestBase httpEntityRequest = (HttpEntityEnclosingRequestBase)request;
            	httpEntityRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            } catch (Exception e) {
            	throw new IllegalArgumentException(e);
            }
        }
	}
	
	
	
	public static HttpRequestBase getRequest(HttpMethod method, String url) throws IllegalArgumentException {
		if (method == HttpMethod.GET_METHOD) {
			return new HttpGet(url);
		} else if (method == HttpMethod.PUT_METHOD) {
			return new HttpPut(url);
		} else if (method == HttpMethod.POST_METHOD) {
			return new HttpPost(url);
		} else if (method == HttpMethod.DELETE_METHOD) {
			return new HttpDelete(url);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
