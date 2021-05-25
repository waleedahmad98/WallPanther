package com.waleed.walle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Send user to MainActivity as soon as this activity loads
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // remove this activity from the stack
        finish();
    }
}
