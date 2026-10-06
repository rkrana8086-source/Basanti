package com.example.voiceassistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("BASANTI JAVA DIAGNOSTIC\n\nSTARTUP OK");
        text.setTextSize(24);
        text.setPadding(40, 80, 40, 40);

        setContentView(text);
    }
}
