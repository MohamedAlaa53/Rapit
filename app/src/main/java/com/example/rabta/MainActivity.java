package com.example.rabta;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private WebView webView;
    private String pendingText = "";
    private String pendingTitle = "";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.setBackgroundColor(0xFFF4F6FB);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("file:///android_asset/")) return false;
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                    return true;
                } catch (Exception e) { return false; }
            }
            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                deliverPendingShare();
            }
        });
        webView.addJavascriptInterface(new ShareBridge(), "AndroidShare");
        setContentView(webView);
        handleIntent(getIntent());
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
        deliverPendingShare();
    }

    private void handleIntent(Intent intent) {
        if (intent == null || !Intent.ACTION_SEND.equals(intent.getAction())) return;
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (text == null) text = "";
        pendingText = text;
        pendingTitle = title == null ? "" : title;
    }

    private void deliverPendingShare() {
        if (webView == null || pendingText.isEmpty()) return;
        final String text = pendingText;
        final String title = pendingTitle;
        pendingText = "";
        pendingTitle = "";
        String js = "if(window.receiveSharedLink){window.receiveSharedLink(" +
            JSONObjectQuote(text) + "," + JSONObjectQuote(title) + ");}";
        webView.post(() -> webView.evaluateJavascript(js, null));
    }

    private String JSONObjectQuote(String s) {
        return org.json.JSONObject.quote(s == null ? "" : s);
    }

    public class ShareBridge {
        @JavascriptInterface public String getSharedText() { return ""; }
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
