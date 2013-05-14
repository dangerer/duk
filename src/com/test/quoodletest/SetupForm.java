package com.test.quoodletest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.test.quoodletest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class SetupForm extends Activity{
	final Activity activity = this;
    private EditText form_login;
	private EditText form_password;
	private EditText form_domain;
	private Button buttonSaveLogin;
	private String login;
	private String password;
	private String domain;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_layout);
         
        activity.setTitle("");
        form_login = (EditText) findViewById(R.id.editText2);
        form_password = (EditText) findViewById(R.id.editText3);
        form_domain = (EditText) findViewById(R.id.editText1); /**/
        
    	try {
    	    BufferedReader in = new BufferedReader(new FileReader(Quoodle.PATH+Quoodle.STORAGEFILENAME));
    	    String str;
    	    if ((str = in.readLine()) != null) {
        	    String[] separated = str.split(",");
        	    login = separated[0].trim(); 
        	    password = separated[1].trim(); 
        	    domain = separated[2].trim();    
        	    form_domain.setText(domain);
        	    form_login.setText(login);
        	    form_password.setText(password);
    	    }
    	    in.close();
    	} catch (IOException e) {
    	}        
        	
        // Setup click listener
        buttonSaveLogin = (Button) findViewById(R.id.buttonSaveLogin);        
        buttonSaveLogin.setOnClickListener( new OnClickListener() {
			public void onClick(View view) {
        		SaveSetup();
        	}
        });        /**/        
    }
	
	private void SaveSetup() {		
		try {
			//File root = android.os.Environment.getExternalStorageDirectory();
			File root = new File(Quoodle.PATH);
			root.mkdirs();
			if (root.canWrite()){
				File f = new File(root, Quoodle.STORAGEFILENAME);
				FileWriter fw = new FileWriter(f);
				BufferedWriter out = new BufferedWriter(fw);
				out.write(form_login.getText()+","+form_password.getText()+","+form_domain.getText());
				out.close();
				//Intent intent = new Intent(SetupForm.this, Quoodle.class);
		        //startActivity(intent);
			}
			} 
		catch (IOException e) {
			}/**/
		Intent intent = new Intent(SetupForm.this, Quoodle.class);
//		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();		
	}  
	
}
