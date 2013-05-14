package com.test.quoodletest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import com.test.quoodletest.R;
import com.test.quoodletest.SetupForm;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Quoodle extends Activity {
	final Activity activity = this;
	private WebView webView;
	public static final String PATH = android.os.Environment
			.getExternalStorageDirectory().getPath() + "/dukapp/";
	public static final String STORAGEFILENAME = "users.cdb";
	private Button buttonSetup;
	private String login;
	private String password;
	private String domain;

	// private ProgressBar progressBar;
	// private Handler barHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_quoodle);

		File file = new File(PATH + STORAGEFILENAME);
		if (file.exists()) {
			goUrl();
		} else {
			Intent intent = new Intent(Quoodle.this, SetupForm.class);
			// startActivity(intent);
			startActivityForResult(intent, 2);
		}

		// Setup click listener
		buttonSetup = (Button) findViewById(R.id.button_setup);
		buttonSetup.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(Quoodle.this, SetupForm.class);
				startActivityForResult(intent, 2);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (resultCode == Activity.RESULT_OK) {
				goUrl();
			}
			;
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void goUrl() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(PATH
					+ STORAGEFILENAME));
			String str;
			if ((str = in.readLine()) != null) {
				String[] separated = str.split(",");
				login = separated[0].trim();
				password = separated[1].trim();
				domain = separated[2].trim();
				String d = domain.toLowerCase();
				if (!d.startsWith("http"))
					domain = "http://" + domain;
			}
			in.close();
		} catch (IOException e) {
		}

		webView = (WebView) findViewById(R.id.webview_compontent);
		webView.loadUrl("about:blank");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.setWebViewClient(new WebViewClient());
		String postData = "username=" + login + "&password=" + password;
		if (domain.endsWith("/"))
			domain += "login/index.php";
		else
			domain += "/login/index.php";

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {

				activity.setTitle("Loading...");
				activity.setProgress(progress * 100);

				if (progress == 100)
					activity.setTitle("");
			}
		});

		webView.postUrl(domain, EncodingUtils.getBytes(postData, "BASE64"));
	}

}
