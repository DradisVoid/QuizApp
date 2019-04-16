package edu.andrews.cptr252.arn.quizapp;

import android.support.v4.app.Fragment;

public class QuizModeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new QuizModeFragment();
    }
}
