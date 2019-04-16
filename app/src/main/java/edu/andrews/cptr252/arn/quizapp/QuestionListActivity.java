package edu.andrews.cptr252.arn.quizapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Activity for displaying list of questions
 */
public class QuestionListActivity extends SingleFragmentActivity
        implements QuestionListFragment.Callbacks, QuestionDetailsFragment.Callbacks {

    @Override
    public void onQuestionSelected(Question question) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            // start an instance of quesitonDetailsActivity
            Intent i = new Intent(this, QuestionDetailsActivity.class);
            i.putExtra(QuestionDetailsFragment.EXTRA_QUESTION_ID, question.getId());
            startActivityForResult(i, 0);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detail_fragment_container);
            Fragment newDetail = QuestionDetailsFragment.newInstance(question.getId());

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }

            ft.add(R.id.detail_fragment_container, newDetail);
            ft.commit();
        }
    }

    public void onQuestionUpdated(Question question) {
        FragmentManager fm = getSupportFragmentManager();
        QuestionListFragment listFragment = (QuestionListFragment)fm.findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    protected Fragment createFragment() {
        return new QuestionListFragment();
    }

    @Override
    protected int getLayourResId() {
        return R.layout.activity_masterdetail;
    }
}
