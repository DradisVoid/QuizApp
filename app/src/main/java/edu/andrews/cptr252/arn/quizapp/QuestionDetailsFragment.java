package edu.andrews.cptr252.arn.quizapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.UUID;


/**
 * Show the details for a question and allow editing
 */
public class QuestionDetailsFragment extends Fragment {
    /** Key used to pass the id of a question */
    public static final String EXTRA_QUESTION_ID = "edu.andrews.cptr252.arn.quizapp.question_id";

    /** Reference to question field */
    private EditText mQuestionField;
    /** Reference to answer radio group */
    private RadioGroup mAnswerRadioGroup;
    /** Reference to Done button */
    private Button mDoneButton;
    /** Question that is being viewed/edited */
    private Question mQuestion;


    public QuestionDetailsFragment() {
        // Required empty public constructor
    }

    /** Required interface to be implemented inhosting activities */
    public interface Callbacks {
        void onQuestionUpdated(Question question);
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

    public void updateQuestion() {
        QuestionList.getInstance(getActivity()).updateQuestion(mQuestion);
        mCallbacks.onQuestionUpdated(mQuestion);
    }

    /**
     * Create a new QuestionDetailsFragment with a given Question id as an argument
     * @param questionId
     * @return A reference to the new QuestionDetailsFragment
     */
    public static QuestionDetailsFragment newInstance(UUID questionId) {
        // Create a new argument Bundle object
        // Add the id as an argument
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_QUESTION_ID, questionId);
        // Create a new instance of QuestionDetailsFragment
        QuestionDetailsFragment fragment = new QuestionDetailsFragment();
        // Pass the bundle (containing the Question id) to the fragment
        // The bundle will be unpacked in the fragment's onCreate(Bundle) method
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Extract the id from the Bundle
        UUID questionId = (UUID)getArguments().getSerializable(EXTRA_QUESTION_ID);

        // Get the question with the id from the Bundle
        // This will be the question that the fragment displays
        mQuestion = QuestionList.getInstance(getActivity()).getQuestion(questionId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_details, container, false);

        // get reference to EditText box for question
        mQuestionField = v.findViewById(R.id.question_text);
        mQuestionField.setText(mQuestion.getQuestion());
        mQuestionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user typed text, update the question
                mQuestion.setQuestion(s.toString());
                updateQuestion();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // intentionally left blank
            }
        });

        // get reference to RadioGroup box for question answer
        mAnswerRadioGroup = v.findViewById(R.id.answer_radioGroup);
        if (mQuestion.getQuestion() == null) {
            mAnswerRadioGroup.check(-1);
        } else {
            mAnswerRadioGroup.check(mQuestion.getAnswer() ? R.id.true_radioButton : R.id.false_radioButton);
        }
        mAnswerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // set the question's answer
                if (checkedId == R.id.true_radioButton){
                    mQuestion.setAnswer(true);
                } else if (checkedId == R.id.false_radioButton){
                    mQuestion.setAnswer(false);
                }
                updateQuestion();
            }
        });

        return v;
    }

    /**
     * Save the question list when app is paused
     */
    @Override
    public void onPause() {
        super.onPause();
        QuestionList.getInstance(getActivity()).updateQuestion(mQuestion);
    }
}
