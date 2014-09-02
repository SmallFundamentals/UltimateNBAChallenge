package com.gmail.blakeyu21.ultimatenba.core.implementation;

import com.gmail.blakeyu21.ultimatenba.core.IQuizQuestion;

import java.util.Random;

/**
 * Created by Blake on 17/02/14.
 */
public class QuizQuestionImpl implements IQuizQuestion
{
    protected int _id;
    protected String _clue1;
    protected String _clue2;
    protected String _clue3;
    protected String[] _multipleChoice;
    protected String _answer;

    public QuizQuestionImpl(int _id, String clue1, String clue2, String clue3, String[] generatedChoices, String _answer)
    {
        setID(_id);
        setClue1(clue1);
        setClue2(clue2);
        setClue3(clue3);
        setAnswer(_answer);

        Random random = new Random();

        int correctIndex = random.nextInt(4);
        String[] choices = new String[4];
        choices[correctIndex] = _answer;

        for (String str : generatedChoices )
        {
            for (int i = 0; i < choices.length; i++)
            {
                if (choices[i] == null)
                {
                    choices[i] = str;
                    break;
                }
            }
        }

        setMultipleChoice(choices);
    }
    public QuizQuestionImpl(int _id, String clue1, String clue2, String clue3, String choice1, String choice2, String choice3, String choice4, String _answer)
    {
        setID(_id);
        setClue1(clue1);
        setClue2(clue2);
        setClue3(clue3);
        setMultipleChoice(choice1, choice2, choice3, choice4);
        setAnswer(_answer);
    }

    public int getID()
    {
        return _id;
    }
    public void setID(int _id)
    {
        this._id = _id;
    }

    public String getClue1()
    {
        return this._clue1;
    }
    public void setClue1(String clue)
    {
        this._clue1 = clue;
    }
    public String getClue2()
    {
        return this._clue2;
    }
    public void setClue2(String clue)
    {
        this._clue2 = clue;
    }
    public String getClue3()
    {
        return this._clue3;
    }
    public void setClue3(String clue)
    {
        this._clue3 = clue;
    }

    public String[] getMultipleChoice()
    {
        return _multipleChoice;
    }
    public void setMultipleChoice(String choice1, String choice2, String choice3, String choice4)
    {
        String[] temp = {choice1, choice2, choice3, choice4};
        _multipleChoice = temp;
    }
    public void setMultipleChoice(String[] _multipleChoice)
    {
        this._multipleChoice = _multipleChoice;
    }

    public String getAnswer()
    {
        return _answer;
    }
    public void setAnswer(String _answer)
    {
        this._answer = _answer;
    }

    public boolean isCorrect(String givenAnswer)
    {
        return _answer == givenAnswer;
    }
    public boolean isCorrect(int index)
    {
        return _answer == _multipleChoice[index];
    }
}
