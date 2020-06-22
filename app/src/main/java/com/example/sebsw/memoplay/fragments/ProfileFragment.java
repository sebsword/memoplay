package com.example.sebsw.memoplay.fragments;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.model.Score;
import com.example.sebsw.memoplay.services.ProgressBarAnimation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int level;
    private int xpOnLevel;
    private int endLevel;
    private int percentage;

    // positions for arrays in the database
    private int wordId;
    private String wordTitle;


    ProgressBar progressBar;
    TextView levelLabel, experienceLabel;
    Button returnBtn;
    TextView wordLabel;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordId = getArguments().getInt("word_id");
            wordTitle = getArguments().getString("word_title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment

        /*

        Redundant code
        ------------------------------------
        endLevel = totalXP / 100;
        maxXP = (endLevel + 1) * 100;
        xp = totalXP - ((endLevel)*100);
        requiredXP = maxXP - ((endLevel)*100);
        double tmpXP = (double)xp;
        double tmpRequiredXP = (double)requiredXP;
        double tmpPercentage = ((tmpXP / tmpRequiredXP)*100);
        percentage = (int)tmpPercentage;
        ------------------------------------
        */

        //Creates a score object so the information about the players experience
        // can be easily retrieved from the database and stored in an accessible format
        Score score = new Score(wordId);
        endLevel = score.getLevel();
        percentage = score.getPercentage();
        xpOnLevel = score.getXpOnLevel();

        levelLabel = (TextView)v.findViewById(R.id.levelLabel);
        wordLabel = (TextView)v.findViewById(R.id.wordLabel);
        experienceLabel = (TextView)v.findViewById(R.id.experienceLabel);

        progressBar = (ProgressBar)v.findViewById(R.id.circularProgressBar);
        Log.d("gg", Integer.toString(progressBar.getProgress()));
        returnBtn = (Button)v.findViewById(R.id.returnBtn);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch CategoriesFragment
                MainActivity.getMainActivity().loadCategoriesFragment();
            }
        });

        wordLabel.setText(wordTitle);
        levelLabel.setText(Integer.toString(0));
        experienceLabel.setText("XP: " + 0);

        progressBar.setProgress(0);
        startCountAnimation();




        return v;
    }


    /*the animations here were hard to implement because they are called in the onCreateView Method
    which lead to me having issues with the view not being drawn until the animation had already
    been completed.
     */
    private void startCountAnimation() {
        ValueAnimator animator = new ValueAnimator();
        //animates from 0 to endLevel
        animator.setObjectValues(0, endLevel );
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                //sets the level displayed to the user to change based on animation value
                levelLabel.setText("" + (int) animation.getAnimatedValue());
                level = (int) animation.getAnimatedValue();

                //once the animation has stopped
                if (level == endLevel){
                    //start animating the progress bar as a percentage of xp at the current level
                    ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, percentage);
                    anim.setDuration(1000);
                    progressBar.startAnimation(anim);

                    //start animating the xp counter
                    ValueAnimator animator1 = new ValueAnimator();
                    //animate the counter from 0 to the totalXP gained on that level
                    animator1.setObjectValues(0, xpOnLevel);
                    animator1.setDuration(1000);
                    animator1.start();
                    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation1) {
                            experienceLabel.setText("XP :" + (int)animation1.getAnimatedValue());

                        }
                    });

                }
            }
        });
        animator.start();
    }

}
