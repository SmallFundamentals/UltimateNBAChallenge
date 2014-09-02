package com.gmail.blakeyu21.ultimatenba.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gmail.blakeyu21.ultimatenba.R;
import com.gmail.blakeyu21.ultimatenba.util.QuizMaster;

public class StartGame extends Activity
{
    protected QuizMaster quizMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Full Screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start_game);

        quizMaster = new QuizMaster(this);
        quizMaster.displayNextQuestion();
    }

    public void answer1Button_Pressed(View view)
    {
        quizMaster.checkAnswer(0);
    }

    public void answer2Button_Pressed(View view)
    {
        quizMaster.checkAnswer(1);
    }

    public void answer3Button_Pressed(View view)
    {
        quizMaster.checkAnswer(2);
    }

    public void answer4Button_Pressed(View view)
    {
        quizMaster.checkAnswer(3);
    }

    public void nextClueButton_Pressed(View view)
    {
        quizMaster.displayNextClue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy()
    {
        quizMaster.dispose();
        super.onDestroy();
    }
}
