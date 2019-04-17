package edu.andrews.cptr252.arn.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuizModeActivity extends FragmentActivity implements QuizModeFragment.Callbacks {
    /** Custom Nonswiping viewpager to handle fragments */
    private NonSwipeableViewPager mViewPager;

    /** Array of questions */
    private ArrayList<Question> mQuestions;

    /** Current score */
    private int mScore = 0;

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
     * Show the correct answer and current score
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

        // Message to display on button (Next Question or Finish)
        String dialogButton;
        if (mViewPager.getCurrentItem() == mQuestions.size() - 1) {
            dialogButton = "Finish";

            // Add final percentage
            double percentageScore = ((double) mScore) / mQuestions.size();

            dialogMessage = dialogMessage + "\n\nFinal Percentage: " +
                    df.format(percentageScore * 100.0) + " %";
        } else {
            dialogButton = "Next Question";
        }

        // Construct Dialog to show correct answer and score
        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                // Move to next fragment or to main screen
                .setNeutralButton(dialogButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

        //Toast.makeText(getApplicationContext(), "" + mScore, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the view pager
        mViewPager = new NonSwipeableViewPager(this);
        // ViewPager needs a resource id
        mViewPager.setId(R.id.nonSwipeableViewPager);
        // Set the ViewPager for this activity to be the ViewPager
        setContentView(mViewPager);

        // get the list of questions
        mQuestions = QuestionList.getInstance(this).getQuestions();

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
