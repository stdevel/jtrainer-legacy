package eu.stdevel.jtrainer;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Browser extends Activity {

	private WebView mWebView;
	
	private WebViewClient mViewClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);
		mWebView = (WebView) findViewById(R.id.wv_helpBrowser);
		mWebView.setWebViewClient(mViewClient);
		mWebView.getSettings().setJavaScriptEnabled(false);
		mWebView.loadUrl("file:///android_asset/readme.html");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//...
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	
}
