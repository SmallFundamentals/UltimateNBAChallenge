package com.gmail.blakeyu21.ultimatenba.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gmail.blakeyu21.ultimatenba.R;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full screen, no notification bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main_menu);
    }

    // Start Button
    public void startGame(View view)
    {
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
    }

    // Options Button
    public void showOptions(View view)
    {
        Intent intent = new Intent(this, ShowOptions.class);
        startActivity(intent);
    }

    // Stats Button
    public void showStats(View view)
    {
        Intent intent = new Intent(this, DisplayStats.class);
        startActivity(intent);
    }
}