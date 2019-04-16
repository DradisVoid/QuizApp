package edu.andrews.cptr252.arn.quizapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

public class QuestionDetailsActivity extends FragmentActivity implements QuestionDetailsFragment.Callbacks {
    /** Viewpager component that allows you to browse by swiping */
    private ViewPager mViewPager;

    /** Array of questions */
    private ArrayList<Question> mQuestions;

    @Override
    public void onQuestionUpdated(Question question) {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the view pager
        mViewPager = new ViewPager(this);
        // ViewPager needs a resource id
        mViewPager.setId(R.id.viewPager);
        // Set the ViewPager for this activity to be the ViewPager
        setContentView(mViewPager);

        // get the list of questions
        mQuestions = QuestionList.getInstance(this).getQuestions();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            // Create a new QuestionDetailsFragment for question at given position in list
            @Override
            public Fragment getItem(int i) {
                Question question = mQuestions.get(i);
                // Create a new instance of the QuestionDetailsFragment
                // with the Question id as an argument
                return QuestionDetailsFragment.newInstance(question.getId());
            }

            @Override
            public int getCount() {
                return mQuestions.size();
            }
        });

        //QuestionListFragment now launches QuestionDetailsActivity with a specific id
        // Get the intent sent to this activity from the QuestionListFragment
        UUID questionId = (UUID)getIntent().getSerializableExtra(QuestionDetailsFragment.EXTRA_QUESTION_ID);

        // Search through the list of bugs until we find the bug with the same
        // id as the one from the intent
        for (int i = 0; i < mQuestions.size(); i++) {
            if (mQuestions.get(i).getId().equals(questionId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
