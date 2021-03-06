package com.example.gauge;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncHttpRequest extends AsyncTask <String, Void, GaugeHttpResponse> {
	IGaugeAsync activity;
	public AsyncHttpRequest(IGaugeAsync mainActivity) {
		super();
		this.activity = mainActivity;
	}
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {

		InputStream in = entity.getContent();

		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			if (n>0) out.append(new String(b, 0, n));
		}
		return out.toString();
	}
	
	public void Login(String email, String password)
	{
		JSONObject jsonArg = new JSONObject();
		try {
			jsonArg.put("Email", email);
			jsonArg.put("Password", password);
		} catch (JSONException e) {
			Log.d("Json building login JSON object exception", e.getMessage());
		}
		this.execute("POST","http://mortgagecalculator.cloudapp.net/api/login", jsonArg.toString());
	}
	
	public void Register(String email, String forename, String surname, String password)
	{
		JSONObject jsonArg = new JSONObject();
		try {
			jsonArg.put("Email", email);
			jsonArg.put("Forename", forename);
			jsonArg.put("Surname", surname);
			jsonArg.put("Password", password);
			jsonArg.put("AccountTypeId", "1");
			jsonArg.put("Active", true);
		} catch (JSONException e) {
			Log.d("Json building register JSON object exception", e.getMessage());
		}
		this.execute("POST","http://mortgagecalculator.cloudapp.net/api/account", jsonArg.toString());
	}
	
	public void RetrieveAccount(int accountId)
	{
		this.execute("GET",String.format("http://mortgagecalculator.cloudapp.net/api/account/%s", accountId));
	}
	
	public void UpdateAccount(int accountId, String email, String forename, String surname)
	{
		JSONObject jsonArg = new JSONObject();
		try {
			jsonArg.put("AccountId", accountId);
			jsonArg.put("Forename", forename);
			jsonArg.put("Surname", surname);
			jsonArg.put("Email", email);
		} catch (JSONException e) {
			Log.d("Json building register JSON object exception", e.getMessage());
		}
		this.execute("PUT",String.format("http://mortgagecalculator.cloudapp.net/api/account/%s", accountId), jsonArg.toString());
	}
	
	public void Calculate(String property_value, String deposit, String term, String interest_rate, String fees, int accountId, UUID customerReference)
	{
		JSONObject jsonArg = new JSONObject();
		try {
			jsonArg.put("AccountId", accountId);
			jsonArg.put("CustomerReference", customerReference);
			jsonArg.put("HouseValue", property_value);
			jsonArg.put("Deposit", deposit);
			jsonArg.put("InterestRate", interest_rate);
			jsonArg.put("Term", term);
			jsonArg.put("Fees", fees);
			jsonArg.put("MortgageType", "repayment");
			jsonArg.put("Source", "Gauge Android App");
		} catch (JSONException e) {
			Log.d("Json building calculate JSON object exception", e.getMessage());
		}
		this.execute("POST","http://mortgagecalculator.cloudapp.net/api/mortgage", jsonArg.toString());
	}
	
	public void Compare(String property_value, String deposit, String term, int accountId, UUID customerReference)
	{
		JSONObject jsonArg = new JSONObject();
		try {
			jsonArg.put("AccountId", accountId);
			jsonArg.put("CustomerReference", customerReference);
			jsonArg.put("HouseValue", property_value);
			jsonArg.put("Deposit", deposit);
			jsonArg.put("Term", term);
			jsonArg.put("MortgageType", "repayment");
			jsonArg.put("Source", "Gauge Android App");
		} catch (JSONException e) {
			Log.d("Json building compare JSON object exception", e.getMessage());
		}
		this.execute("POST","http://mortgagecalculator.cloudapp.net/api/mortgage", jsonArg.toString());
	}
	
	public void Email(int calculationId, String emailAddress)
	{
		JSONObject jsonArg = new JSONObject();
		try {
			jsonArg.put("CalculationId", calculationId);
			jsonArg.put("Address", emailAddress);
		} catch (JSONException e) {
			Log.d("Json building email JSON object exception", e.getMessage());
		}
		this.execute("POST","http://mortgagecalculator.cloudapp.net/api/email", jsonArg.toString());
	}
	
	public void Favourite(int calculationId)
	{
		this.execute("PUT",String.format("http://mortgagecalculator.cloudapp.net/api/mortgage/%s", calculationId));
	}

	@Override
	protected GaugeHttpResponse doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		GaugeHttpResponse reponseObject = new GaugeHttpResponse(0,params[0], "");
		
		HttpRequestBase request = null;
		
		if(params[0] == "POST") {
			HttpPost httpPost = new HttpPost(params[1]);
			httpPost.setHeader("Content-Type", "application/json");
			try {
				httpPost.setEntity(new StringEntity(params[2]));
			} catch (UnsupportedEncodingException ex) {
				Log.d("Building httpPost - Unable to encode JsonArgs as StringEntity", ex.getMessage());
			}
			request = httpPost;
		} else if(params[0] == "PUT") {
			HttpPut httpPut = new HttpPut(params[1]);
			httpPut.setHeader("Content-Type", "application/json");
			if(params.length > 2) {
				try {
					httpPut.setEntity(new StringEntity(params[2]));
				} catch (UnsupportedEncodingException ex) {
					Log.d("Building httpPut - Unable to encode JsonAnrgs as StringEntity", ex.getMessage());
				}
			}
			request = httpPut;
		} else {
			HttpGet httpGet = new HttpGet(params[1]);
			request = httpGet;	 
		}
		
		String text = null;
		try {	
			HttpResponse response = httpClient.execute(request, localContext);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
			reponseObject = new GaugeHttpResponse(statusCode, params[0], text);
		} catch (Exception ex) {
			Log.d("Building httpPost - Unable to encode JsoArgs as StringEntity", ex.getMessage());
		}
		return reponseObject;
	}

	protected void onPostExecute(GaugeHttpResponse results) {
		this.activity.handleResponse(results);
	}
}