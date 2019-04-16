package edu.andrews.cptr252.arn.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;

import edu.andrews.cptr252.arn.quizapp.database.QuestionCursorWrapper;
import edu.andrews.cptr252.arn.quizapp.database.QuestionDbHelper;
import edu.andrews.cptr252.arn.quizapp.database.QuestionDbSchema.QuestionTable;

/**
 * Manage list of bugs. This is a singleton class.
 */
public class QuestionList {
    /** Instance variable for QuestionList */
    private static QuestionList sOurInstance;

    /** SQLite DB where questions are stored */
    private SQLiteDatabase mDatabase;

    public static ContentValues getContentValues(Question question) {
        ContentValues values = new ContentValues();
        values.put(QuestionTable.Cols.UUID, question.getId().toString());
        values.put(QuestionTable.Cols.QUESTION, question.getQuestion());
        values.put(QuestionTable.Cols.ANSWER, question.getAnswer() ? 1 : 0);

        return values;
    }

    private QuestionCursorWrapper queryQuestions(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuestionTable.NAME,
                null,   // Columns - null selects all
                whereClause,
                whereArgs,
                null,   // groupBy
                null,   // having
                null    // orderBy
        );

        return new QuestionCursorWrapper(cursor);
    }

    /** Reference to information about app environment */
    private Context mAppContext;

    /**
     * Add a question to the list
     * @param question is the question to be added
     */
    public void addQuestion(Question question) {
        ContentValues values = getContentValues(question);
        mDatabase.insert(QuestionTable.NAME, null, values);
    }

    /**
     * Update question information
     * @param question contains the latest information
     */
    public void updateQuestion(Question question) {
        String uuidString = question.getId().toString();
        ContentValues values = getContentValues(question);

        mDatabase.update(QuestionTable.NAME, values, QuestionTable.Cols.UUID + " = ? ",
                new String[] { uuidString } );
    }

    /**
     * Delete a given question from list
     * @param question is the question to delete
     */
    public void deleteQuestion(Question question) {
        String uuidString = question.getId().toString();
        mDatabase.delete(QuestionTable.NAME, QuestionTable.Cols.UUID + " = ? ",
                new String[] { uuidString } );
    }

    private QuestionList(Context appContext) {
        mAppContext = appContext.getApplicationContext();

        // Open DB file or create it if it does not already exist
        // If the DB is an older version, onUpgrade will be called
        mDatabase = new QuestionDbHelper(mAppContext).getWritableDatabase();
    }

    /**
     * Return one and only instance of the question list
     * @param c is the Application context
     * @return Reference to the question list
     */
    public static QuestionList getInstance(Context c) {
        if (sOurInstance == null) {
            sOurInstance = new QuestionList(c.getApplicationContext());
        }
        return sOurInstance;
    }

    /**
     * Return list of questions
     * @return Array of question objects
     */
    public ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        QuestionCursorWrapper cursor = queryQuestions(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                questions.add(cursor.getQuestion());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return questions;
    }

    /**
     * Return the question with the given id
     * @param id Unique id for the question
     * @return Question object or null if not found
     */
    public Question getQuestion(UUID id) {
        QuestionCursorWrapper cursor = queryQuestions(QuestionTable.Cols.UUID + " = ? ",
                new String[] { id.toString() } );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getQuestion();
        } finally {
            cursor.close();
        }
    }
}
