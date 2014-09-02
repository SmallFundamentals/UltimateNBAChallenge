package com.gmail.blakeyu21.ultimatenba.core;

/**
 * An interface that describes a quiz _question.
 * Created by Blake on 17/02/14.
 */
public interface IQuizQuestion
{
    public int getID();
    public void setID(int id);
    public String getClue1();
    public void setClue1(String clue);
    public String getClue2();
    public void setClue2(String clue);
    public String getClue3();
    public void setClue3(String clue);
    public String[] getMultipleChoice();
    public void setMultipleChoice(String choice1, String choice2, String choice3, String choice4);
    public void setMultipleChoice(String[] multipleChoice);
    public String getAnswer();
    public void setAnswer(String answer);
    public boolean isCorrect(String givenAnswer);
    public boolean isCorrect(int index);
}
