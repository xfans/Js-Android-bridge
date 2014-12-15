package com.example.xfans.js.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.xfans.js.R;
import com.example.xfans.js.bridge.XfansWebView;
import com.example.xfans.js.widget.XfansBaseActivity;


public class MainActivity extends XfansBaseActivity {
    private XfansWebView webView;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (XfansWebView) findViewById(R.id.webView);
        button = (Button) findViewById(R.id.btn);
        setWebView(webView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(MainActivity.this,"com.example.xfans.js.sample.TestActivity");
                MainActivity.this.startActivity(intent);
            }
        });
        webView.loadUrl("file:///android_asset/index.html");

        Log.d("XfanWebChromeClient", "------------");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
