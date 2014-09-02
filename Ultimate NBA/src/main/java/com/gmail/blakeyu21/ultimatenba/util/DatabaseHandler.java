package com.gmail.blakeyu21.ultimatenba.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.blakeyu21.ultimatenba.core.implementation.QuizQuestionImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Blake on 17/02/14.
 */

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static String DB_PATH = "/data/data/com.gmail.blakeyu21.ultimatenba/databases/";
    private static String DB_NAME = "quizQuestionsManager.db";

    private SQLiteDatabase database;
    private final Context context;

    // Question Set table
    private static final String TABLE_QUESTIONSET = "questionSet";
    // Table column names
    private static final String KEY_ID = "_id";
    private static final String KEY_CLUE1 = "clue1";
    private static final String KEY_CLUE2 = "clue2";
    private static final String KEY_CLUE3 = "clue3";
    private static final String KEY_ANSWER = "answer";

    // Players
    private static final String TABLE_PLAYERS = "players";
    // Table column names
    private static final String KEY_PLAYER_NAME = "player_name";

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHandler(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    /**
     * CHANGED: Always overwrite database.
     *  --- If the database does not already exist, create it and copy it over.---
     * */
    public void createDatabase() throws IOException
    {
        if(true)//!checkDatabase())
        {
            this.getReadableDatabase();
            try
            {
                copyDatabase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if database already exists
     * @return true if database exists
     */
    private boolean checkDatabase()
    {
        File file = context.getDatabasePath(DB_NAME);
        return file.exists();
    }

    /**
     * Copy database from assets to system folder
     * */
    private void copyDatabase() throws IOException
    {
        InputStream inputStream = context.getAssets().open(DB_NAME);
        String outfile = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outfile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer))>0)
        {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public void openDataBase()
    {
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close()
    {
        if(database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    // CRUD OP. FOR QUESTION SET TABLE
    public void addQuestionSet(QuizQuestionImpl quizQuestion)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLUE1, quizQuestion.getClue1());
        values.put(KEY_CLUE2, quizQuestion.getClue2());
        values.put(KEY_CLUE3, quizQuestion.getClue3());
        values.put(KEY_ANSWER, quizQuestion.getAnswer());
        database.insert(TABLE_QUESTIONSET, null, values);

        database.close();
    }

    public QuizQuestionImpl getQuestionSet(int id)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_QUESTIONSET, new String[] { KEY_ID, KEY_CLUE1, KEY_CLUE2, KEY_CLUE3, KEY_ANSWER },
                KEY_ID + "=?", new String[] { String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // Generates a question with standard question and answer, but false answers are random.
        QuizQuestionImpl quizQuestion = new QuizQuestionImpl(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), getRandomPlayers(3, cursor.getString(4)), cursor.getString(4));

        cursor.close();

        return quizQuestion;
    }

    public List<String> getAllQuestionSets()
    {
        List<String> players = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONSET;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do
            {
                players.add(cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    public int getQuestionSetCount()
    {
        String query = "SELECT * FROM " + TABLE_QUESTIONSET;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        int ret = cursor.getCount();
        cursor.close();
        return ret;
    }

    public int updateQuestionSet(QuizQuestionImpl quizQuestion)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLUE1, quizQuestion.getClue1());
        values.put(KEY_CLUE2, quizQuestion.getClue1());
        values.put(KEY_CLUE3, quizQuestion.getClue1());
        values.put(KEY_ANSWER, quizQuestion.getAnswer());

        return database.update(TABLE_QUESTIONSET, values, KEY_ID + " =? ", new String[] {String.valueOf(quizQuestion.getID())});
    }

    public void deleteQuestionSet(QuizQuestionImpl quizQuestion)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_QUESTIONSET, KEY_ID + " =?", new String[] { String.valueOf(quizQuestion.getID()) });
        database.close();
    }

    // CRUD OP. FOR PLAYERS TABLE
    public void addPlayer(String playerName)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, playerName);
        database.insert(TABLE_PLAYERS, null, values);

        database.close();
    }

    public String[] getRandomPlayers(int numberOfPlayers, String cannotContain)
    {
        String[] ret = getRandomPlayers(numberOfPlayers);
        while(Arrays.asList(ret).contains(cannotContain))
        {
            ret = getRandomPlayers(numberOfPlayers);
        }

        return ret;
    }

    public String[] getRandomPlayers(int numberOfPlayers)
    {
        String[] players = new String[numberOfPlayers];
        int[] usedID = new int[numberOfPlayers];
        Random random = new Random();
        SQLiteDatabase database = this.getReadableDatabase();

        for (int i = 0; i < numberOfPlayers; i++)
        {
            int id = -1;
            boolean isDuplicate = true;
            while(isDuplicate)
            {
                id = random.nextInt(getPlayerCount()) + 1;

                isDuplicate = false;
                for (int j = 0; j < numberOfPlayers; j++)
                {
                    if (usedID[j] == id)
                    {
                        isDuplicate = true;
                        continue;
                    }
                }
            }

            usedID[i] = id;

            Cursor cursor = database.query(TABLE_PLAYERS, new String[] { KEY_ID, KEY_PLAYER_NAME},
                    KEY_ID + "=?", new String[] { String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            players[i] = cursor.getString(1);
            cursor.close();
        }

        return players;
    }

    public String getPlayer(int id)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_PLAYERS, new String[] { KEY_ID, KEY_PLAYER_NAME},
                KEY_ID + "=?", new String[] { String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String playerName = cursor.getString(1);
        cursor.close();
        return playerName;
    }

    public List<String> getAllPlayers()
    {
        List<String> players = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do
            {
                players.add(cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    public int getPlayerCount()
    {
        String query = "SELECT * FROM " + TABLE_PLAYERS;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        int ret = cursor.getCount();
        cursor.close();
        return ret;
    }

    public int updatePlayer(String playerName, int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, playerName);

        return database.update(TABLE_PLAYERS, values, KEY_ID + " =? ", new String[] {String.valueOf(id)});
    }

    public void deletePlayer(int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_PLAYERS, KEY_ID + " =?", new String[] { String.valueOf(id) });
        database.close();
    }
}
