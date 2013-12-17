package com.ugurcakmak.gelistiricikonsolu;

import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {
	private static String url = "https://play.google.com/apps/publish/";
	private static int shortToast = Toast.LENGTH_SHORT;
	private Context context = this;

	WebView devBrowser;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);

		devBrowser = (WebView) findViewById(R.id.webView_main);
		devBrowser.getSettings().setJavaScriptEnabled(true);
		devBrowser.getSettings().setSaveFormData(false);
		devBrowser.getSettings().setSavePassword(true);
		devBrowser.getSettings().setAppCacheEnabled(true);

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
		
		if (activeNetwork != null && activeNetwork.isConnected()) {
			try {
				devBrowser.loadUrl(url);
				final ProgressDialog progress = new ProgressDialog(context);
				
				if (Locale.getDefault().getLanguage().equals("tr")) {
					progress.setMessage("Yükleniyor...");
				} else {
					progress.setMessage("Loading...");
				}
				
				progress.setCanceledOnTouchOutside(false);
				progress.setCancelable(false);
				progress.show();
				
				devBrowser.setWebViewClient(new WebViewClient() {
					@Override
			         public void onPageFinished(WebView view, String url) {
			            super.onPageFinished(view, url);
			            progress.dismiss();
			         }					
				});
			} catch (Exception e) {
				Toast.makeText(context,
						R.string.warning_error_loading + e.toString(),
						shortToast).show();
			}
		} else {
			Toast.makeText(context, R.string.warning_error_connection, shortToast).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh: {
			try {
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetwork = connMgr
						.getActiveNetworkInfo();

				if (activeNetwork != null && activeNetwork.isConnected()) {
					devBrowser.reload();
					
					final ProgressDialog progress = new ProgressDialog(context);
					if (Locale.getDefault().getLanguage().equals("tr")) {
						progress.setMessage("Yükleniyor...");
					} else {
						progress.setMessage("Loading...");
					}
					progress.setCanceledOnTouchOutside(false);
					progress.setCancelable(false);
					progress.show();
					
					devBrowser.setWebViewClient(new WebViewClient() {
						@Override
				         public void onPageFinished(WebView view, String url) {
				            super.onPageFinished(view, url);
				            progress.dismiss();
				         }					
					});
				} else {
					Toast.makeText(context, R.string.warning_error_loading, shortToast)
							.show();
				}
			} catch (Exception e) {
				Toast.makeText(context,
						R.string.warning_error_connection + e.toString(),
						shortToast).show();
			}
		}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
