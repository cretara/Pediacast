package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myapplication.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    String s = "";
    TTSManager ttsManager = null;
    String deviceID = "1234";
    String codice = "";
    String lingua = "";
    String[] ArayMessaggio = null;
    String domain = "https://preprodpt20.pediatotem.it";
    String paginatotem = domain + "/pediacast/index.html";
    String sottopagina = "totem";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState != null) {
            //importante anti doppio
            System.exit(2);

        } else {
            ttsManager = new TTSManager();
            ttsManager.init(this);
            CookieSyncManager.createInstance(this);
            CookieSyncManager.getInstance().startSync();
            deviceID = Build.SERIAL;
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(domain, "PediaTotem=" + deviceID + "; path=/" + sottopagina);
            CookieSyncManager.getInstance().sync();
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commitNow();
            }

            webView = findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(paginatotem);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

            //coont += 1;
            //webView = (WebView) findViewById(R.id.webview);
            //WebSettings webSettings = myWebView.getSettings();
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }
            //webView.setWebViewClient(new MyWebViewClient());//per aggiornare pagina con click

            //webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onLoadResource(WebView view, String url) {
                    if (url.indexOf("comando.php") > 0) {
                        //Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();

                        String NomeFile = url.split("comando.php")[1];
                        //    Toast.makeText (MainActivity.this, "comando" + NomeFile, Toast.LENGTH_SHORT).show();
//                                      s = s.replaceAll("GET /", "");
//                                         s = s.replaceAll("HTTP/1.1", "");
                        s = com.example.myapplication.UTFEncodingUtil.decodeUTF(NomeFile);
                        codice = s.split("~")[1];
                        lingua = s.split("~")[0];
                        //  Toast.makeText (MainActivity.this, "codice" + codice + ". lingua: " + lingua, Toast.LENGTH_SHORT).show();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  mp.start();
                                //Toast bread = Toast.makeText(getApplicationContext(), codice, Toast.LENGTH_LONG);
                                //bread.show();
                                ttsManager.addQueue(codice, lingua);
                            }
                        });
                    }
                /*if (url.indexOf("stampa.php") > 0) {
                    Toast.makeText (MainActivity.this, url, Toast.LENGTH_SHORT).show();
                    String NomeFile = url.split("stampa.php")[1];
                    s = com.example.myapplication.UTFEncodingUtil.decodeUTF(NomeFile);
                    ArayMessaggio = s.split("~");
                    //  Toast.makeText (MainActivity.this, "codice" + codice + ". lingua: " + lingua, Toast.LENGTH_SHORT).show();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Stampa(ArayMessaggio);
                        }
                    });
                }*/
                }
             /*   @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    if (url.indexOf("comando.php")>0) {
                        Toast.makeText(MainActivity.this, "aaa" + url, Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }*/

            });
        }
    }
}
