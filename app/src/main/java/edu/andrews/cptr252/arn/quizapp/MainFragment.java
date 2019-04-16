package edu.andrews.cptr252.arn.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Fragment to display buttons to edit questions or start quiz
 */
public class MainFragment extends Fragment {
    /** Reference to Button to start quiz */
    Button startQuizButton;

    /** Reference to Button to edit questions */
    Button editQuestionsButton;

    public MainFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Set reference to start quiz button
        startQuizButton = v.findViewById(R.id.startQuizButton);
        // Set button to execute an intent to start the Quiz Mode Activity
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startQuizIntent = new Intent(getActivity(), QuizModeFragment.class);
                startActivity(startQuizIntent);
            }
        });

        // Set reference to edit questions button
        editQuestionsButton = v.findViewById(R.id.editButton);
        // Set button to execute an intent to star the Question List Activity to view and edit questions
        editQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editQuestionsIntent = new Intent(getActivity(), QuestionListActivity.class);
                startActivity(editQuestionsIntent);
            }
        });

        return v;
    }
}
