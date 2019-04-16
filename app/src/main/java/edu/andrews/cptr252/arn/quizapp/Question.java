package edu.andrews.cptr252.arn.quizapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Manage information for a specified question
 */
public class Question {
    /** Unique Id for the Question */
    private UUID mId;
    /** Question text of question */
    private String mQuestion;
    /** Answer for question */
    private boolean mAnswer;

    /** JSON attribute for question if */
    private static final String JSON_ID = "id";
    /** JSON attribute for question text */
    private static final String JSON_QUESTION = "question";
    /** JSON attribute for answer */
    private static final String JSON_ANSWER = "answer";

    /**
     * Initialize a new question from a JSON object
     * @param json is the JSON object for a question
     * @throws JSONException
     */
    public Question(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mQuestion = json.optString(JSON_QUESTION);
        mAnswer = json.getBoolean(JSON_ANSWER);
    }

    /**
     * Write the question to a JSON object
     * @return JSON object containing the question information
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_ID, mId.toString());
        jsonObject.put(JSON_QUESTION, mQuestion);
        jsonObject.put(JSON_ANSWER, mAnswer);

        return jsonObject;
    }

    /**
     * Create and initialize a new question
     * @param id UUID for new question
     */
    public Question(UUID id) {
        mId = id;
    }

    /**
     * Create and initialize a new Question with random UUID
     */
    public Question() {
        this(UUID.randomUUID());
    }

    /**
     * Return id for question
     * @return Question ID
     */
    public UUID getId() {
        return mId;
    }

    /**
     * Get the question text
     * @return Question text
     */
    public String getQuestion() {
        return mQuestion;
    }

    /**
     * Provide a new question text
     * @param question New question
     */
    public void setQuestion(String question) {
        mQuestion = question;
    }

    /**
     * Return question's answer
     * @return True/False answer
     */
    public boolean getAnswer() {
        return mAnswer;
    }

    /**
     * Provide a new answer
     * @param answer New True/False answer
     */
    public void setAnswer(boolean answer) {
        mAnswer = answer;
    }
}
