package com.example.webviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.webviewexample.Constants.HTML_STRING;

public class DisplayHTMLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_h_t_m_l);

        Intent intent=getIntent();
        String htmlString=intent.getStringExtra(HTML_STRING);

        TextView htmlTextView=findViewById(R.id.htmlTextView);
        htmlTextView.setText(htmlString);
    }
}