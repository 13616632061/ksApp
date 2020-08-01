package com.ui.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConnectionManager {

	private  ArrayList <NameValuePair> params;
	private  ArrayList <NameValuePair> headers;
	private String url;

	public ConnectionManager(String url) {
	    this.url = url;
	    params = new ArrayList<NameValuePair>();
	    headers = new ArrayList<NameValuePair>();
	}

	public void addParam(String name, String value)
	{
	    params.add(new BasicNameValuePair(name, value));
	}

	public void addHeader(String name, String value)
	{
	    headers.add(new BasicNameValuePair(name, value));
	}

	public String sendRequest() throws Exception {
	    String serverResponse = "";
	    HttpPost httpPostRequest = new HttpPost(url);
	    httpPostRequest.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	    //add headers
	    for(int i = 0; i < headers.size(); i++) {
	        StringEntity entity = new StringEntity(headers.get(i).getValue(),"UTF-8");
	        httpPostRequest.setEntity(entity);
	    }

	    if(!params.isEmpty()){
	        HttpEntity httpEntity = new UrlEncodedFormEntity(params,HTTP.UTF_8);
	        httpPostRequest.setEntity(httpEntity);
	    }

	    serverResponse = executeRequest(httpPostRequest);
	    return serverResponse;
	}

	private String executeRequest(HttpUriRequest request) throws Exception  {

	    HttpParams params = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(params, 30000);
	    HttpConnectionParams.setSoTimeout(params, 30000);
	    DefaultHttpClient client = new DefaultHttpClient(params);

	    HttpResponse httpResponse;
	    String serverResponse = "";
	    httpResponse = client.execute(request);
	    HttpEntity entity = httpResponse.getEntity();
	    if (entity != null) {
	        InputStream instream = entity.getContent();
	        serverResponse = convertStreamToString(instream);
	        instream.close();
	    }
//	    Log.d("server response", serverResponse);
	    return serverResponse;
	}

	private String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}

