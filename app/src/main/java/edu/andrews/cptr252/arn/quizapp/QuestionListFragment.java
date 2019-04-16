package edu.andrews.cptr252.arn.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Fragment to display list of questions
 */
public class QuestionListFragment extends ListFragment {
    /** Reference to adapter that generates views for questions */
    private QuestionAdapter mAdapter;

    public QuestionListFragment() {
        // Required empty public constructor
    }

    /**
     * Adapter responsible for getting the view for a question
     */
    private class QuestionAdapter extends ArrayAdapter<Question> {
        public QuestionAdapter(ArrayList<Question> questions) {
            super(getActivity(), 0, questions);
        }

        /**
         * Return the view for a given question in the list
         * @param position Position of view in the list
         * @param convertView Existing view that can still be used
         * @param parent THe layout that contains the views generated
         * @return View containing bug information
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_question, null);
            }

            // Configure the view for this question
            Question question = getItem(position);

            // set the question text for the question in the view
            TextView questionTextView =
                    convertView.findViewById(R.id.question_list_item_textView);
            questionTextView.setText(question.getQuestion());
            // set the answer for the question in the view
            TextView answerTextView =
                    convertView.findViewById(R.id.question_list_item_answerTextView);
            answerTextView.setText(question.getAnswer() ? "True" : "False");

            return convertView;
        }

        public void setQuestions(ArrayList<Question> questions) {
            clear();
            addAll(questions);
        }
    } // end QuestionAdapter

    /** Required interface to be implemented in hsoting activities */
    public interface Callbacks {
        void onQuestionSelected(Question question);
    }

    private Callbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /** Create a new question, add it to the list, and launch the editor */
    private void addQuesiton() {
        // create the new question
        Question question = new Question();
        // add to the list
        QuestionList.getInstance(getActivity()).addQuestion(question);

        // Let the activity decide to launch details fragment or update question
        // in current details fragment (in case of split screen)
        mCallbacks.onQuestionSelected(question);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present
        inflater.inflate(R.menu.menu_question_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_question:
                // new question icon clicked
                addQuesiton();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Update the displayed question list */
    public void updateUI() {
        QuestionList questionList = QuestionList.getInstance(getActivity());
        ArrayList<Question> questions = questionList.getQuestions();

        if (mAdapter == null) {
            mAdapter = new QuestionAdapter(questions);
            setListAdapter(mAdapter);
        } else {
            mAdapter.setQuestions(questions);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.question_list_label);

        // use our custom adapter to generate views
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        // get ListView for the ListFragment
        ListView listView = v.findViewById(android.R.id.list);

        updateUI();

        // allow user to select multiple questions in the list
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // nothing to do
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.question_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // nothing to do
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_question:
                        QuestionList questionList = QuestionList.getInstance(getActivity());
                        // check each question in the list
                        // If it is selected delete the question
                        for (int i = mAdapter.getCount() - 1; i >= 0; i--) {
                            if (getListView().isItemChecked(i)) {
                                // Bug has been selected. Delete it
                                questionList.deleteQuestion(mAdapter.getItem(i));
                            }
                        }
                        mode.finish();
                        // We have changed the question list
                        // Update the list of views
                        updateUI();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // nothing to do
            }
        });

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Question question = (Question)(getListAdapter()).getItem(position);

        // Let the activity decide how selected question is displayed
        mCallbacks.onQuestionSelected(question);
    }

    /**
     * Question list fragment was paused (Likely user is editing a question)
     * Notify the adapter that the data set may have changed
     * The adapter should update the views
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
