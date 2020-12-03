package com.example.webviewexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import timber.log.Timber;

import static com.example.webviewexample.Constants.HTML_STRING;

public class MainActivity extends AppCompatActivity  {
    Button b1;
    EditText ed1;

    private WebView wv1;

    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());

        b1=(Button)findViewById(R.id.button);
        ed1=(EditText)findViewById(R.id.editText);
        ed1.setText("https://english.api.rakuten.net/skyscanner/api/skyscanner-flight-search/details");

        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progDailog = ProgressDialog.show(MainActivity.this, "Loading","Please wait...", true);
                progDailog.setCancelable(false);
                String url = ed1.getText().toString();
                WebSettings webSettings=wv1.getSettings();
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setLoadsImagesAutomatically(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.getAllowUniversalAccessFromFileURLs();

//                wv1.getSettings().setLoadsImagesAutomatically(true);
//                wv1.getSettings().setJavaScriptEnabled(true);
                wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                Log.i("MainActivity","before loadUrl");

                wv1.loadUrl(url);

            }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("MainActivity url= ",url);
            //second url to be loaded in webview parse json here


            //parseHTML(url);
            view.loadUrl(url);
//            return true;
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            if(url.equals("https://rapidapi.com/skyscanner/api/skyscanner-flight-search/endpoints")) {

                new MyTask(url).execute(null, null, null);
            }
            progDailog.dismiss();

        }
    }
//
//    @Override
//    public void onReceivedSslError(WebView view, SslErrorHandler handler,
//                                   SslError error) {
//
//        switch (error.getPrimaryError()) {
//            case SslError.SSL_UNTRUSTED:
//                LogUtility.debug("SslError : The certificate authority is not trusted.");
//                break;
//            case SslError.SSL_EXPIRED:
//                LogUtility.debug("SslError : The certificate has expired.");
//                break;
//            case SslError.SSL_IDMISMATCH:
//                LogUtility.debug("The certificate Hostname mismatch.");
//                break;
//            case SslError.SSL_NOTYETVALID:
//                LogUtility.debug("The certificate is not yet valid.");
//                break;
//        }
//        handler.proceed();
//    }





    private class MyTask extends AsyncTask<Void, Void, Document> {
        String url;

        public MyTask(String url) {
            this.url=url;
        }

        @Override
        protected Document doInBackground(Void... params) {
            String title ="";
            Document doc=null;
            try {
                Timber.i("first try statement");
                doc = Jsoup.connect(url).get();
                Timber.i("able to get doc");
                title = doc.title();
                Timber.i("title= "+title);
            }    //error fetching url
            catch (HttpStatusException ex) {
                Timber.e("Unable to find url");
                //...
            }
            //error getting Document element from HTML
            catch (IOException e) {
                Timber.e("unable to get Document");
                e.printStackTrace();
            }
            return doc;
        }


        @Override
        protected void onPostExecute(Document doc) {
            //if you had a ui element, you could display the title
            if(doc!=null){
                Elements links = doc.select("body");

                for(Element e: links){
                    Timber.i(e.text());
                }
            }
            else{
                Timber.i("doc equals null");
            }
//            ((TextView)findViewById (R.id.myTextView)).setText (result);
        }
    }

    public void parseHTML(String url){
        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
        }
        //error fetching url
        catch (HttpStatusException ex) {
            Timber.e("Unable to find url");
            //...
        }
        //error getting Document element from HTML
        catch (IOException e) {
            Timber.e("unable to get Document");
            e.printStackTrace();
        }

        if(doc!=null){
            Elements links = doc.select("a");

            for(Element e: links){
                Timber.i(e.text());
            }
        }



    }
}



















//public class MainActivity extends AppCompatActivity {
//
//    Button b1;
//    EditText ed1;
//
//    private WebView myWebView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        b1 = (Button) findViewById(R.id.button);
//        ed1 = (EditText) findViewById(R.id.editText);
//        ed1.setText("https://www.tutorialspoint.com/index.htm");
//
//        myWebView = (WebView) findViewById(R.id.webView);
////            wv1.setWebViewClient(new MyBrowser());
//        myWebView.setWebViewClient(new MyWebViewClient());
//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = ed1.getText().toString();
//
//                myWebView.getSettings().setLoadsImagesAutomatically(true);
//                myWebView.getSettings().setJavaScriptEnabled(true);
//                myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//
//                viewSource();
//
//
//                myWebView.loadUrl(url);
//            }
//        });
//    }
//
////        private class MyBrowser extends WebViewClient {
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                view.loadUrl(url);
////                return true;
////            }
////        }
//
//    public class MyWebViewClient extends WebViewClient {
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            Log.i("MainActivity ", "shouldOverridUrlLoading called");
//            if (url.startsWith("source://")) {
//                try {
//                    String html = URLDecoder.decode(url, "UTF-8").substring(9);
//                    sourceReceived(html);
//                } catch (UnsupportedEncodingException e) {
//                    Log.e("example", "failed to decode source", e);
//                }
//                myWebView.getSettings().setJavaScriptEnabled(false);
//                return true;
//            }
//            // For all other links, let the WebView do it's normal thing
//            return false;
//        }
//    }
//
//
//    public void viewSource() {
//        //myWebView.getSettings().setJavaScriptEnabled(true);
//        myWebView.loadUrl(
////                "javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
//                "javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.innerHTML);");
//    }
//
//    private void sourceReceived(String html) {
//        Log.i("MainActivity","sourceReceived called");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(html);
//        builder.setTitle("View Source");
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        Intent intent=new Intent(this, DisplayHTMLActivity.class);
//        intent.putExtra(HTML_STRING, html);
//        startActivity(intent);
//
//
//    }
//
//
//}