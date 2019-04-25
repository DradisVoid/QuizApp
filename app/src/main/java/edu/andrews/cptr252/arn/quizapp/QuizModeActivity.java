package edu.andrews.cptr252.arn.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizModeActivity extends FragmentActivity implements QuizModeFragment.Callbacks {
    /** Custom Nonswiping viewpager to handle fragments */
    private NonSwipeableViewPager mViewPager;

    /** Array of questions */
    private ArrayList<Question> mQuestions;

    /** Constant key to retrieve score between screen rotations */
    private static final String CONSTANT_SCORE = "edu.andrews.cptr252.arn.quizapp.score";

    /** Current score */
    private int mScore = 0;

    /** Constant key to retrieve dialog information after screen rotation */
    private static final String CONSTANT_DIALOG = "edu.andrews.cptr252.arn.quizapp.dialog";

    /** String array holding title, message, and button text for Score Dialog */
    String[] mDialogStrings = new String[3];

    /** DecimalFormat for percentages */
    private static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * If answer was correct, increase score
     */
    @Override
    public void onCorrectAnswer() {
        mScore ++;
        showCurrentScore(true);
    }

    /**
     * If answer was incorrect, do not change score
     */
    @Override
    public void onWrongAnswer() {
        showCurrentScore(false);
    }

    /**
     * Show the user if their answer was correct.
     * Show the current score and progress
     * Give buttons to continue or quit
     * @param wasAnswerCorrect Was the user's answer correct
     */
    private void showCurrentScore(boolean wasAnswerCorrect) {
        // Title to display in dialog (Correct or Incorrect)
        String dialogTitle;
        if (wasAnswerCorrect) {
            dialogTitle = "Correct";
        } else {
            dialogTitle = "Incorrect";
        }

        // Message to display with correct answer, score, and progress
        String dialogMessage = "Correct answer: " +
                (mQuestions.get(mViewPager.getCurrentItem()).getAnswer() ? "True" : "False") +
                "\nCurrent Score: " + mScore + " / " + mQuestions.size() +
                "\n\nProgress: " + (mViewPager.getCurrentItem() + 1) + " / " + mQuestions.size();

        // Show final percentage if last question
        if (mViewPager.getCurrentItem() == mQuestions.size() - 1) {
            // Add final percentage
            double percentageScore = ((double) mScore) / mQuestions.size();

            dialogMessage = dialogMessage + "\n\nFinal Percentage: " +
                    df.format(percentageScore * 100.0) + " %";
        }

        // Message to display on button (Next Question or Finish)
        String dialogButton;
        if (mViewPager.getCurrentItem() == mQuestions.size() - 1) {
            dialogButton = "Finish";
        } else {
            dialogButton = "Next Question";
        }

        showScoreDialog(dialogTitle, dialogMessage, dialogButton);

        //Toast.makeText(getApplicationContext(), "" + mScore, Toast.LENGTH_SHORT).show();
    }

    /**
     * Create and show an AlertDialog with specified Title, Message, and Button, and a Quit button
     * @param dialogTitle Title of dialog
     * @param dialogMessage Message to display in dialog
     * @param dialogButton Text for first button
     */
    private void showScoreDialog(String dialogTitle, String dialogMessage, String dialogButton) {
        // store parameters in String[] for backup
        mDialogStrings[0] = dialogTitle;
        mDialogStrings[1] = dialogMessage;
        mDialogStrings[2] = dialogButton;

        // Construct Dialog to show correct answer and score
        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                // Move to next fragment or to main screen
                .setNeutralButton(dialogButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Reset DialogStrings to empty array
                        mDialogStrings = new String[3];

                        nextFragment();
                    }
                })
                // Quit quiz to main screen
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * Recreate Score Dialog from String[] backup
     */
    private void showScoreDialogFromBackup() {
        showScoreDialog(mDialogStrings[0], mDialogStrings[1], mDialogStrings[2]);
    }

    /**
     * Move viewpager to next question
     * If at end of questions, exit to main menu
     */
    private void nextFragment() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem < mQuestions.size() - 1) {
            mViewPager.setCurrentItem(currentItem + 1);
        } else {
            finish();
        }
    }

    /**
     * Save score and dialog text to savedInstanceState to keep score during rotation
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save mScore
        savedInstanceState.putInt(CONSTANT_SCORE, mScore);

        // Save Dialog Strings
        savedInstanceState.putStringArray(CONSTANT_DIALOG, mDialogStrings);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore score
            mScore = savedInstanceState.getInt(CONSTANT_SCORE);

            // Restore dialog strings
            mDialogStrings = savedInstanceState.getStringArray(CONSTANT_DIALOG);

            if (mDialogStrings[0] != null) {
                showScoreDialogFromBackup();
            }
        }

        // create the view pager
        mViewPager = new NonSwipeableViewPager(this);
        // ViewPager needs a resource id
        mViewPager.setId(R.id.nonSwipeableViewPager);
        // Set the ViewPager for this activity to be the ViewPager
        setContentView(mViewPager);

        // get the list of questions
        mQuestions = QuestionList.getInstance(this).getQuestions();

        if (mQuestions.size() == 0) {
            Toast.makeText(this, "No questions found", Toast.LENGTH_LONG).show();
            finish();
        }

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            // Create a new QuizModeFragment for question at given position in list
            @Override
            public Fragment getItem(int i) {
                Question question = mQuestions.get(i);
                // Create a new instance of the QuizModeFragment
                // with the Question id as an argument
                return QuizModeFragment.newInstance(question.getId());
            }

            @Override
            public int getCount() {
                return mQuestions.size();
            }
        });

        // Set the viewpager to the first question in the list
        mViewPager.setCurrentItem(0);
    }
}
