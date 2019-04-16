package edu.andrews.cptr252.arn.quizapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity for displaying the main page when app is started
 */
public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}
