package com.example.gauge;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CalculateActivity extends DrawerActivity  implements IGaugeAsync{
	SharedPreferences prefs;
	Button calculateBtn;
	AlertDialog alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buildSideNavigation(R.layout.activity_calculate);	
		prefs = getSharedPreferences("gauge_app", MODE_PRIVATE);	
		
		calculateBtn = ( Button ) findViewById(R.id.btn_calculate);		
		calculateBtn.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {
		    	  EditText ET_house_value = (EditText) findViewById(R.id.fld_house_value);
		    	  EditText ET_deposit = (EditText) findViewById(R.id.fld_deposit);
		    	  EditText ET_term = (EditText) findViewById(R.id.fld_term);
		    	  EditText ET_interest_rate = (EditText) findViewById(R.id.fld_interest_rate);
		    	  EditText ET_fees = (EditText) findViewById(R.id.fld_fees);
		    	  String house_value = ET_house_value.getText().toString();
		    	  String deposit = ET_deposit.getText().toString();
		    	  String term = ET_term.getText().toString();
		    	  String interest_rate = ET_interest_rate.getText().toString();
		    	  String fees = ET_fees.getText().toString();
		    	  int accountId = 0;
		    	  UUID customerReference = UUID.fromString("00000000-0000-0000-0000-000000000000");
		    	  if(prefs.getInt("AccountId", 0) != 0) {
		    		  accountId = prefs.getInt("AccountId", 0);
		    	  } else if(prefs.getString("CustomerReference", null) != null) {
		    		  customerReference = UUID.fromString(prefs.getString("CustomerReference", null));
		    	  } else {
		    		  customerReference = UUID.randomUUID();
		    		  Editor edit = prefs.edit();
		    		  edit.putString("CustomerReference", customerReference.toString());
		    		  edit.commit();
		    	  }		    
		    	  if(inputValid(house_value, deposit, term, interest_rate, fees)){
			    	  calculateBtn.setClickable(false);
			    	  new AsyncHttpRequest(CalculateActivity.this).Calculate(house_value,deposit,term,interest_rate,fees,accountId,customerReference);
			    	  createPopUp();
		    	  }
		      }
		});
	}
	
	private Boolean inputValid(String house_value, String deposit, String term, String interest_rate, String fees) {
		ArrayList<String> invalidFields = new ArrayList<String>();
		
		if(house_value.isEmpty()) {
			invalidFields.add("House value");
		}
		if(deposit.isEmpty()) {
			invalidFields.add("Deposit");
		}
		if(term.isEmpty()) {
			invalidFields.add("Term");
		}
		if(interest_rate.isEmpty()) {
			invalidFields.add("Interest rate");
		}
		if(fees.isEmpty()) {
			invalidFields.add("Fees");
		}
		if(invalidFields.size() > 0) {
			String message = buildErrorMessage(invalidFields);
			Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		return true;
	}
	
	private void createPopUp() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalculateActivity.this);
  	  	alert = alertBuilder.create();
  	  	alert.setTitle("Calculating");
  	  	alert.setCancelable(false);
  	  	alert.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(prefs.getInt("AccountId", 0) != 0) {
			getMenuInflater().inflate(R.menu.main, menu);
		} else {
			getMenuInflater().inflate(R.menu.guest, menu);			
		}
		return true;
	}

	@Override
	public void handleResponse(GaugeHttpResponse response) {
		alert.cancel();
		if (response.statusCode == 201) {
			Intent intent = new Intent(CalculateActivity.this, CalculationResultActivity.class);
			try {
				JSONObject jsonResult = new JSONObject(response.content);
				int calculationId = Integer.parseInt(jsonResult.get("CalculationId").toString());
				String monthlyRepayment = jsonResult.get("MonthlyRepayment").toString();
				String loanToValue = jsonResult.get("LoanToValue").toString();
				String totalPaid = jsonResult.get("TotalPaid").toString();
				String totalInterest = jsonResult.get("TotalInterest").toString();
				String houseValue = jsonResult.get("HouseValue").toString();
				String deposit = jsonResult.get("Deposit").toString();
				String term = jsonResult.get("Term").toString();
				String fees = jsonResult.get("Fees").toString();
				String interestRate = jsonResult.get("InterestRate").toString();
	
				intent.putExtra("Calculation_Id", calculationId);
				intent.putExtra("Monthly_Repayment", monthlyRepayment);
				intent.putExtra("Loan_To_Value", loanToValue);
				intent.putExtra("Total_Paid", totalPaid);
				intent.putExtra("Total_Interest", totalInterest);	
				intent.putExtra("House_Value", houseValue);
				intent.putExtra("Deposit", deposit);
				intent.putExtra("Term", term);
				intent.putExtra("Fees", fees);	
				intent.putExtra("Interest_Rate", interestRate);	
			} catch (JSONException e) {
				Log.d("Json parse exception", e.getMessage());
			}  	    		    	  
			startActivity(intent);
	    } else {
			Toast toast = Toast.makeText(getApplicationContext(), "Error, Try again", Toast.LENGTH_LONG);
			toast.show();	    	
	    }
  	  	calculateBtn.setClickable(true);
	}
}
