package gleap.io.gleap;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import gleap.io.gleap.R;

public class GleapMainActivity extends AppCompatActivity implements OnHttpResponseListener {
    private WebView webView;
    private String url = "https://widget.bugbattle.io/appwidgetv5/" + GleapConfig.getInstance().getSdkKey();
    private boolean isWebViewLoaded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            getSupportActionBar().hide();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onCreate(savedInstanceState);

        String postfixUrl = "";
        GleapBug.getInstance().setLanguage(Locale.getDefault().getLanguage());

        url += GleapURLGenerator.generateURL();

        setContentView(R.layout.activity_gleap_main);
        webView = findViewById(R.id.bb_webview);
        webView.setVisibility(View.INVISIBLE);

        try {
            int color = Color.parseColor("#" + GleapConfig.getInstance().getColor().replace("#", ""));
            ((ProgressBar) findViewById(R.id.bb_progressBar))
                    .getIndeterminateDrawable()
                    .setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initBrowser();
    }


    private void initBrowser() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new BugBattleWebViewClient());
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.addJavascriptInterface(new BugBattleJSBridge(this), "BugBattleJSBridge");
        webView.setWebChromeClient(new BugBattleWebChromeClient());
        webView.loadUrl(url);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public void onTaskComplete(int httpResponse) {
        if (httpResponse == 201) {
            GleapDetectorUtil.resumeAllDetectors();
            GleapBug.getInstance().setDisabled(false);
            webView.evaluateJavascript("BugBattle.default.getInstance().showSuccessAndClose()",null);
            //TODO: CB Web Frontend
        } else {
            GleapDetectorUtil.resumeAllDetectors();
            //TODO: CB Web Frontend
            finish();
        }
    }

    private class BugBattleWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.contains("https://widget.bugbattle.io/")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.cancel();
        }

        public void onPageFinished(WebView view, String url) {
            if (isWebViewLoaded) {
                webView.setVisibility(View.VISIBLE);
            }
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            isWebViewLoaded = false;
            webView.setVisibility(View.GONE);

            AlertDialog alertDialog = new AlertDialog.Builder(GleapMainActivity.this).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isWebViewLoaded = true;
                    finish();
                }
            }).create();

            alertDialog.setTitle(getString(R.string.gleap_alert_no_internet_title));
            alertDialog.setMessage(getString(R.string.gleap_alert_no_internet_subtitle));


            alertDialog.show();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }
    }

    private class BugBattleWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                  final JsPromptResult result) {
            return true;
        }


    }

    private class BugBattleJSBridge {
        private final AppCompatActivity mContext;

        public BugBattleJSBridge(AppCompatActivity c) {
            mContext = c;
        }

        @JavascriptInterface
        public void closeBugBattle(String object){
            this.mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GleapDetectorUtil.resumeAllDetectors();
                    finish();
                }
            });
        }

        @JavascriptInterface
        public void requestScreenshot(String option){
            this.mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String image = "data:image/png;base64," + ScreenshotUtil.bitmapToBase64(GleapBug.getInstance().getScreenshot());
                    webView.evaluateJavascript("BugBattle.default.setScreenshot('" + image + "', true)", null);
                }
            });
        }


        @JavascriptInterface
        public void customActionCalled(String object) {

            try {
                JSONObject jsonObject = new JSONObject(object);
                String method = jsonObject.getString("name");
                GleapConfig.getInstance().getCustomActions().invoke(method);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @JavascriptInterface
        public void openExternalURL(String object) {
            try {
                JSONObject jsonObject = new JSONObject(object);
                String url = jsonObject.getString("url");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @JavascriptInterface
        public void sendFeedback(String object) {
            this.mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GleapBug gleapBug = GleapBug.getInstance();
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        String base64String = jsonObject.get("screenshot").toString();
                        String base64Image = base64String.split(",")[1];
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        gleapBug.setScreenshot(decodedByte);
                        gleapBug.setData(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new HttpHelper(GleapMainActivity.this, getApplicationContext(), false).execute(gleapBug);

                }
            });
        }

        @JavascriptInterface
        public void injectScreenshot(String base64) {
            System.out.println(base64);
        }
    }
}