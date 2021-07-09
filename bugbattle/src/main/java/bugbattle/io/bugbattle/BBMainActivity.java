package bugbattle.io.bugbattle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Locale;

public class BBMainActivity extends AppCompatActivity implements OnHttpResponseListener {
    private ProgressBar progressBar;
    private WebView webView;
    private String url = "https://widget.bugbattle.io/appwidget/" + BugBattleConfig.getInstance().getSdkKey();
    private boolean isWebViewLoaded = true;

    private ImageEditor imageEditor;
    private boolean fromEditor = false;
    private boolean editorIsFirstScreen = false;

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
        BugBattleBug.getInstance().setLanguage(Locale.getDefault().getLanguage());
        BugBattleBug bug = BugBattleBug.getInstance();
        BugBattleConfig config = BugBattleConfig.getInstance();

        try {
            if (bug.getEmail() != null) {
                postfixUrl += "?email=" + URLEncoder.encode(bug.getEmail(), "utf-8");
            }

            if (bug.getLanguage() != null) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&lang=" + URLEncoder.encode(bug.getLanguage(), "utf-8");
                } else {
                    postfixUrl += "?lang=" + URLEncoder.encode(bug.getLanguage(), "utf-8");
                }
            }

            if (config.isPrivacyPolicyEnabled()) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&enableprivacypolicy=" + config.isPrivacyPolicyEnabled();
                } else {
                    postfixUrl += "?enableprivacypolicy=" + config.isPrivacyPolicyEnabled();
                }
            }

            if (!config.getPrivacyPolicyUrl().equals("")) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&privacyplicyurl=" + URLEncoder.encode(config.getPrivacyPolicyUrl(), "utf-8");
                } else {
                    postfixUrl += "?privacyplicyurl=" + URLEncoder.encode(config.getPrivacyPolicyUrl(), "utf-8");
                }
            }
            if (!config.getColor().equals("")) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&color=" + URLEncoder.encode(config.getColor().replace("#", ""), "utf-8");
                } else {
                    postfixUrl += "?color=" + URLEncoder.encode(config.getColor().replace("#", ""), "utf-8");
                }
            }
            //logourl=xxxxx&showpoweredby=true
            if (config.getLogoUrl() != null && config.getLogoUrl() != "") {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&logourl=" + URLEncoder.encode(config.getLogoUrl(), "utf-8");
                } else {
                    postfixUrl += "?logourl=" + URLEncoder.encode(config.getLogoUrl(), "utf-8");
                }
            }


            if (postfixUrl.length() > 0) {
                postfixUrl += "&showpoweredby=" + config.isShowPoweredBy();
            } else {
                postfixUrl += "?showpoweredby=" + config.isShowPoweredBy();
            }
            url += postfixUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_b_b_main);
        progressBar = findViewById(R.id.bb_loadingBar);
        int color = Color.parseColor("#398cfe");
        try {
            color = Color.parseColor("#" + BugBattleConfig.getInstance().getColor().replace("#", ""));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ProgressBar) findViewById(R.id.bb_loadingBar))
                    .getIndeterminateDrawable()
                    .setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        webView = findViewById(R.id.bb_webview);
        webView.setVisibility(View.INVISIBLE);

        findViewById(R.id.bb_btnback).setVisibility(View.GONE);
        findViewById(R.id.bb_btncancle).setVisibility(View.GONE);
        findViewById(R.id.bb_next).setVisibility(View.GONE);
        initBrowser();
        initButtons(color);
        try {
            ImageView img = findViewById(R.id.bb_success_img);
            img.setColorFilter(color);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Drawable setDrawableColor(Drawable drawable, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            return drawable;
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            return drawable;
        }
    }

    private void initButtons(int color) {
        ((Button) findViewById(R.id.bb_btncancle)).setTextColor(color);
        ((Button) findViewById(R.id.bb_btnback)).setTextColor(color);
        ((Button) findViewById(R.id.bb_next)).setTextColor(color);

        findViewById(R.id.bb_btncancle).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    BBDetectorUtil.resumeAllDetectors();
                    finish();
                }
                //  overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                return false;
            }
        });

        findViewById(R.id.bb_btnback).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    if (editorIsFirstScreen) {
                        findViewById(R.id.bb_btnback).setVisibility(View.GONE);
                        findViewById(R.id.bb_feedback).setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.INVISIBLE);
                        findViewById(R.id.bb_btncancle).setVisibility(View.GONE);
                        findViewById(R.id.bb_next).setVisibility(View.INVISIBLE);
                        webView.loadUrl(url);
                        webView.reload();
                    } else {
                        if (fromEditor) {
                            fromEditor = false;
                            findViewById(R.id.bb_feedback).setVisibility(View.VISIBLE);
                            findViewById(R.id.bb_next).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.bb_btnback).setVisibility(View.GONE);
                            findViewById(R.id.bb_feedback).setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            findViewById(R.id.bb_btncancle).setVisibility(View.GONE);
                            findViewById(R.id.bb_next).setVisibility(View.INVISIBLE);
                            webView.loadUrl(url);
                            webView.reload();
                        }
                    }
                }
                return false;
            }
        });

        findViewById(R.id.bb_next).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    fromEditor = true;
                    Bitmap editedImage = imageEditor.getEditedImage();
                    if (editedImage != null) {
                        BugBattleBug.getInstance().setScreenshot(editedImage);
                        findViewById(R.id.bb_feedback).setVisibility(View.INVISIBLE);
                        findViewById(R.id.bb_btncancle).setVisibility(View.GONE);
                        findViewById(R.id.bb_btnback).setVisibility(View.VISIBLE);
                        findViewById(R.id.bb_next).setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
    }

    private void initBrowser() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        webView.addJavascriptInterface(new BugBattleJSBridge(this), "BugBattleJSBridge");
        webView.setWebViewClient(new BugBattleWebViewClient());
        webView.setWebChromeClient(new BugBattleWebChromeClient());
        webView.loadUrl(url);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public void onTaskComplete(int httpResponse) {
        if (httpResponse == 201) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            BBDetectorUtil.resumeAllDetectors();
                            BugBattleBug.getInstance().setDisabled(false);
                            findViewById(R.id.bb_loadingBar).setVisibility(View.INVISIBLE);
                            findViewById(R.id.bb_success).setVisibility(View.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    finish();
                                }
                            }, 1500);


                        }
                    }, 10);
        } else {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            BBDetectorUtil.resumeAllDetectors();
                            findViewById(R.id.bb_loadingBar).setVisibility(View.INVISIBLE);
                            findViewById(R.id.bb_error).setVisibility(View.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    finish();
                                }
                            }, 2000);


                        }
                    }, 10);

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
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            //     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //    startActivity(intent);
            //    return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public void onPageFinished(WebView view, String url) {
            if (isWebViewLoaded) {
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                findViewById(R.id.bb_btncancle).setVisibility(View.VISIBLE);
            }
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            isWebViewLoaded = false;
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            findViewById(R.id.bb_btncancle).setVisibility(View.INVISIBLE);
            AlertDialog alertDialog = new AlertDialog.Builder(BBMainActivity.this).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isWebViewLoaded = true;
                    finish();
                }
            }).create();

            alertDialog.setTitle(getString(R.string.bb_alert_no_internet_title));
            alertDialog.setMessage(getString(R.string.bb_alert_no_internet_subtitle));


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
        public void selectedMenuOption(String option) {
            this.mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContext.findViewById(R.id.bb_btncancle).setVisibility(View.GONE);
                    mContext.findViewById(R.id.bb_btnback).setVisibility(View.VISIBLE);
                }
            });
        }

        @JavascriptInterface
        public void openScreenshotEditor(String metadata) {
            this.mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(metadata);
                        editorIsFirstScreen = jsonObject.getBoolean("screenshotEditorIsFirstStep");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (imageEditor == null) {
                        imageEditor = new ImageEditor(mContext);
                        imageEditor.init();
                    }
                    findViewById(R.id.bb_next).setVisibility(View.VISIBLE);
                    findViewById(R.id.bb_feedback).setVisibility(View.VISIBLE);
                }
            });
        }


        @JavascriptInterface
        public void customActionCalled(String object) {

            try {
                JSONObject jsonObject = new JSONObject(object);
                String method = jsonObject.getString("name");
                BugBattleConfig.getInstance().getCustomActions().invoke(method);
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
                    findViewById(R.id.bb_loadingBar).setVisibility(View.VISIBLE);
                    findViewById(R.id.bb_webview).setVisibility(View.INVISIBLE);
                    findViewById(R.id.bb_btncancle).setVisibility(View.INVISIBLE);
                    findViewById(R.id.bb_btnback).setVisibility(View.INVISIBLE);
                    findViewById(R.id.bb_next).setVisibility(View.INVISIBLE);
                    BugBattleBug bugBattleBug = BugBattleBug.getInstance();
                    new HttpHelper(BBMainActivity.this, getApplicationContext(), false).execute(bugBattleBug);
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        bugBattleBug.setData(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}