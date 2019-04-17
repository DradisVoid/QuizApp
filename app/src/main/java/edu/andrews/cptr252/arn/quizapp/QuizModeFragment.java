package edu.andrews.cptr252.arn.quizapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;


/**
 * Show a question and ask for an answer
 */
public class QuizModeFragment extends Fragment {
    /** Key used to pass the id of a question */
    public static final String EXTRA_QUESTION_ID = "edu.andrews.cptr252.arn.quizapp.quiz_question_id";

    /** Reference to TextView for showing Question */
    private TextView questionTextView;
    /** Reference to RadioGroup for answer choices */
    private RadioGroup answerRadioGroup;
    /** Reference to Done button */
    private Button doneButton;

    /** Question being answered */
    private Question mQuestion;
    /** Answer to question */
    private Boolean mAnswer;

    public QuizModeFragment() {
        // Required empty public constructor
    }

    /** Required interface to be implemented in hosting activities */
    public interface Callbacks {
        void onCorrectAnswer();
        void onWrongAnswer();
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

    public void answerQuestion() {
        if (mQuestion.getAnswer() == mAnswer) {
            mCallbacks.onCorrectAnswer();
        } else {
            mCallbacks.onWrongAnswer();
        }
    }

    /**
     * Create a new fragment with given Question id
     * @param questionId
     * @return A reference to the new QuizModeFragment
     */
    public static QuizModeFragment newInstance(UUID questionId) {
        // Create a new argument Bundle object
        // Add the id as an argument
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_QUESTION_ID, questionId);
        // Create a new instance of QuizModeFragment
        QuizModeFragment fragment = new QuizModeFragment();
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
        View v = inflater.inflate(R.layout.fragment_quiz_mode, container, false);

        // get question textview
        questionTextView = v.findViewById(R.id.question_textView);
        questionTextView.setText(mQuestion.getQuestion());

        // get radiogroup for answers
        answerRadioGroup = v.findViewById(R.id.answer_radioGroup);

        // get Done button
        doneButton = v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerRadioGroup.getCheckedRadioButtonId() == R.id.true_radioButton) {
                    mAnswer = true;
                    answerQuestion();
                } else if (answerRadioGroup.getCheckedRadioButtonId() == R.id.false_radioButton) {
                    mAnswer = false;
                    answerQuestion();
                } else {
                    mAnswer = null;
                    Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT);
                }
            }
        });

        return v;
    }

}
