package edu.andrews.cptr252.arn.quizapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import edu.andrews.cptr252.arn.quizapp.Question;
import edu.andrews.cptr252.arn.quizapp.database.QuestionDbSchema.QuestionTable;

/**
 * Handle results from DB queries
 */
public class QuestionCursorWrapper extends CursorWrapper {
    public QuestionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Question getQuestion() {
        String uuidString = getString(getColumnIndex(QuestionTable.Cols.UUID));
        String question = getString(getColumnIndex(QuestionTable.Cols.QUESTION));
        int answer = getInt(getColumnIndex(QuestionTable.Cols.ANSWER));

        Question mQuestion = new Question(UUID.fromString(uuidString));
        mQuestion.setQuestion(question);
        mQuestion.setAnswer(answer != 0);
        return mQuestion;
    }
}
