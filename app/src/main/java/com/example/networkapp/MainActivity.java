package com.example.networkapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.DropBoxManager;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainHttp";
    private EditText url;
    private TextView name;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = (EditText) findViewById(R.id.name_url);
        name = (TextView) findViewById(R.id.name_text);


    }


    public void fetch_data(View view) {
        String url_string = url.getText().toString();
        ConnectivityManager Conn_man = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Conn_man.getActiveNetworkInfo();
        Toast.makeText(this,"Connecting...",Toast.LENGTH_SHORT).show();

        if (networkInfo != null && networkInfo.isConnected()) {
            new WebpageDownload().execute(url_string);
        } else {
            name.setText("Network Connection Not Available");
        }

    }

    private class WebpageDownload extends AsyncTask<String, Void, String> {
        @Override

        protected String doInBackground(String... urls) {
           try {
             return downloadUrl(urls[0]);

           }
        catch (IOException e) {
                return "Unable to Retrieve Webpage. URL Entered may be invalid";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            name.setText(result);
            //WebView myWebView = (WebView) findViewById(R.id.webview);
            //myWebView.loadData(result, "text/html", null);

        }

        private String downloadUrl(String input_url) throws  IOException {
            InputStream is = null;
            int len = 450;

            Document doc;

            try {


                URL url = new URL(input_url);
                HttpURLConnection new_connection = (HttpURLConnection) url.openConnection();
                new_connection.setReadTimeout(10000 /* milliseconds */);
                new_connection.setConnectTimeout(15000 /* milliseconds */);
                new_connection.setRequestMethod("GET");
                new_connection.setDoInput(true);

                new_connection.connect();
                int answer = new_connection.getResponseCode();
                Log.d(TAG, "This is the Answer: " + answer);
                is = new_connection.getInputStream();
                doc = Jsoup.connect(input_url).get();
                String convert_to_str = str_con(is, len);

                title = doc.title();
                System.out.println("title : " + title);
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    System.out.println("\nlink : " + link.attr("href"));
                    System.out.println("text : " + link.text());
                }
                return title;
                /*
                //We will put the data into a StringBuilder
                StringBuilder builder=new StringBuilder();

                URL url = new URL("https://gdata.youtube.com/feeds/api/standardfeeds/top_rated");

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp=factory.newPullParser();

                xpp.setInput(getInputStream(url), "UTF_8");

                int eventType=xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT){
                    // Looking for a start tag
                    if(eventType==XmlPullParser.START_TAG){
                        //We look for "title" tag in XML response
                        if(xpp.getName().equalsIgnoreCase("title")){
                            //Once we found the "title" tag, add the text it contains to our builder
                            builder.append(xpp.nextText()+"\n");
                        }
                    }

                    eventType=xpp.next();
                }
*/

            } finally {
                if (is != null) {
                    is.close();
                }
            }


        }
        public String str_con(InputStream stream, int len) throws IOException, UnsupportedEncodingException {

            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);





        }

    }



    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("Text", name.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        name.setText(savedInstanceState.getString("Text"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Toast.makeText(this,"landscape",Toast.LENGTH_SHORT).show();

        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Toast.makeText(this,"portrait",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "Inside OnStart");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG,"Inside OnPause");

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"Inside OnREsume");



    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "Inside OnSTop");

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Inside OnDestroy");
    }



}












