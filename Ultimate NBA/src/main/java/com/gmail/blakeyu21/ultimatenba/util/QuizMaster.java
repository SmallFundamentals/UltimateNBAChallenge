package com.gmail.blakeyu21.ultimatenba.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.blakeyu21.ultimatenba.R;
import com.gmail.blakeyu21.ultimatenba.core.IQuizQuestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Blake on 01/03/14.
 */
public class QuizMaster
{
    protected enum Stat { CORRECTS, ATTEMPTS, POINTS }

    protected int THREE_POINTER = 3;
    protected int NUMBER_OF_QUESTIONS;
    protected int cluesDisplayed = 0;
    protected Map<Stat, Integer> statkeeper = new HashMap<Stat, Integer>();
    protected Random random;
    protected Activity caller;
    protected DatabaseHandler helper;

    protected TextView[] clues = new TextView[3];
    protected Button[] buttons = new Button[4];
    protected TextView scoreboard;
    protected List<Integer> currentSessionUsedQuestionID = new ArrayList<Integer>();
    protected Button nextClueButton;

    protected IQuizQuestion currentQuestion;

    public QuizMaster(Activity caller)
    {
        this.caller = caller;
        initialize();
    }

    public void initialize()
    {
        random = new Random();
        helper = new DatabaseHandler(caller);
        try
        {
            helper.createDatabase();
        }
        catch (IOException ioe)
        {
            throw new Error("Unable to create database");
        }
        helper.openDataBase();
        NUMBER_OF_QUESTIONS = helper.getQuestionSetCount();
        clues[0] = (TextView) caller.findViewById(R.id.clue1);
        clues[1] = (TextView) caller.findViewById(R.id.clue2);
        clues[2] = (TextView) caller.findViewById(R.id.clue3);
        buttons[0] = (Button) caller.findViewById(R.id.answer1);
        buttons[1] = (Button) caller.findViewById(R.id.answer2);
        buttons[2] = (Button) caller.findViewById(R.id.answer3);
        buttons[3] = (Button) caller.findViewById(R.id.answer4);
        nextClueButton = (Button) caller.findViewById(R.id.nextClueButton);
        scoreboard = (TextView) caller.findViewById(R.id.scoreboard);

        statkeeper.put(Stat.ATTEMPTS, 0);
        statkeeper.put(Stat.CORRECTS, 0);
        statkeeper.put(Stat.POINTS, 0);
        updateScoreboard();
    }

    public void dispose()
    {
        helper.close();
    }

    public void displayNextQuestion()
    {
        cluesDisplayed = 0;
        nextClueButton.setEnabled(true);
        if(isOutOfQuestions())
        {
            Log.d("Check", "Out of questions: " + currentSessionUsedQuestionID.size() + ". Resetting");
            currentSessionUsedQuestionID = new ArrayList<Integer>();
        }

        int newQuestionID = random.nextInt(NUMBER_OF_QUESTIONS) + 1;
        while (Arrays.asList(currentSessionUsedQuestionID).contains(newQuestionID))
        {
            newQuestionID = random.nextInt(NUMBER_OF_QUESTIONS) + 1;
        }

        Log.d("Message", "Obtaining new question: " + newQuestionID);
        currentQuestion = helper.getQuestionSet(newQuestionID);

        Log.d("Message", "Setting new clue");
        clues[0].setText(currentQuestion.getClue1());
        clues[1].setText(null);
        clues[2].setText(null);
        cluesDisplayed++;

        Log.d("Message", "Obtaining new choices and setting them");
        String[] multipleChoice = currentQuestion.getMultipleChoice();
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].setText(multipleChoice[i]);
        }
    }

    public void displayNextClue()
    {
        if(cluesDisplayed++ == 1)
        {
            clues[1].setText(currentQuestion.getClue2());
        }
        else
        {
            clues[2].setText(currentQuestion.getClue3());
            nextClueButton.setEnabled(false);
        }
    }

    public void checkAnswer(int index)
    {
        statkeeper.put(Stat.ATTEMPTS, statkeeper.get(Stat.ATTEMPTS) + 1);
        if (currentQuestion.isCorrect(index))
        {
            statkeeper.put(Stat.CORRECTS, statkeeper.get(Stat.CORRECTS) + 1);
            statkeeper.put(Stat.POINTS, statkeeper.get(Stat.POINTS) + THREE_POINTER - (cluesDisplayed - 1));
        }
        updateScoreboard();
        currentSessionUsedQuestionID.add(currentQuestion.getID());
        displayNextQuestion();
    }

    public void updateScoreboard()
    {
        scoreboard.setText("PLAYER1 - " + statkeeper.get(Stat.POINTS) + " PTS");
    }

    protected boolean isOutOfQuestions()
    {
        return currentSessionUsedQuestionID.size() == NUMBER_OF_QUESTIONS;
    }
}
