package edu.andrews.cptr252.arn.quizapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.andrews.cptr252.arn.quizapp.database.QuestionDbSchema.QuestionTable;

/**
 * Methods for creating/updating Question DB
 */
public class QuestionDbHelper extends SQLiteOpenHelper {
    /** Current DB version. Increment as DB structure changes */
    private static final int VERSION = 1;
    /** DB Filename */
    private static final String DATABASE_NAME = "questionDb.db";

    public QuestionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * DB does not exist. Create it
     * @param db will contain the new database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute SQL command to create a new bug database
        db.execSQL("create table " + QuestionTable.NAME + "(" +
                " _id integer primary key autoincrement," +
                QuestionTable.Cols.UUID + ", " +
                QuestionTable.Cols.QUESTION + ", " +
                QuestionTable.Cols.ANSWER +
                ")");
    }

    /**
     * Previous DB is older and needs to be upgraded to current version
     * @param db is the database to be upgraded
     * @param oldVersion is the version number of the DB
     * @param newVersion is the version the DB should be upgraded to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
