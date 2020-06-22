package com.example.sebsw.memoplay.model;

import android.util.Log;

import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.services.CategoryDataService;
import com.example.sebsw.memoplay.services.MyDBHandler;

import java.util.ArrayList;

/**
 * Created by sebsw on 22/11/2016.
 */

public class Score {
    private int totalXP;
    private final double xpConst = 0.3;
    private int level;
    private int minimumXP;
    private int maximumXP;
    private int wordId;
    private int xpToAdd = 0;
    private int xpOnLevel;
    private int percentage;
    private final String TAG = "vevev";

    //Main class constructor, sets up score data based on the ID of a word in the database
    public Score (int wordId){
        this.wordId = wordId;
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);
        this.totalXP = dbHandler.getTotalXP(wordId);
        Log.d(TAG, "so the total XP retrieved was " + this.totalXP);
        setLevel();
        setMinimumXP();
        setMaximumXP();
        setXpOnLevel();
        setPercentage();
    }

    /*Constructor made using method overloading, takes two parameters instead of one,
     the additional parameter being the amount of xp to add to the total score
     in the database*/
    public Score (int wordId, int xpToAdd){
        this.wordId = wordId;
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);
        this.totalXP = dbHandler.getTotalXP(wordId);
        Log.d(TAG, "so the total XP retrieved was " + this.totalXP);
        this.xpToAdd = xpToAdd;
        setLevel();
        setMinimumXP();
        setMaximumXP();
        setXpOnLevel();
        setPercentage();
    }

    //method to update score in the database
    public void updateScore(){
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);
        Log.d(TAG, "updateScore: will add " + xpToAdd + " to the word with id " + wordId );
        dbHandler.updateScore(wordId,level,xpToAdd);
    }

    //resets score for a particular word in the database
    public void resetScore(){
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);
        Log.d(TAG, "setting your level to 0" );
        dbHandler.resetScore(wordId);
    }
    //sets level based on the total amount of XP inputted as a parameter.
    public void setLevel(){
        double tmpLevel = xpConst* Math.sqrt(totalXP);
        this.level = (int)tmpLevel;
        Log.d(TAG, "setLevel:" + tmpLevel);
    }

    public int getLevel(){
        return level;
    }

    /*gets the XP required to reach a specific level
    This function is the mathematical inverse of the formula
    needed to calculate a level */
    public int getXPfromLevel(double level){
        double tmpXP = Math.pow((level / xpConst), 2);
        return (int)tmpXP;
    }

    //calculates minimum XP needed to reach a level
    private void setMinimumXP(){
        this.minimumXP = getXPfromLevel(level);
    }

    public int getMinimumXP(){
        return minimumXP;
    }

    //Finds the maximum XP that a user can have before they level up
    private void setMaximumXP(){
        this.maximumXP = getXPfromLevel(level + 1);
        Log.d(TAG, "setMaximumXP: ");
    }

    public int getMaximumXP(){
        return maximumXP;
    }

    /* Finds how much XP a player has gained since reaching their current level */
    public void setXpOnLevel(){
        this.xpOnLevel = totalXP - minimumXP;
        Log.d(TAG, "setXpOnLevel: " + xpOnLevel);
    }

    public int getXpOnLevel(){
        return xpOnLevel;
    }

    /*Expresses a players XP gain on a current level as a fraction of the totalXP
    they would need to level up and reach the next level so that e.g. at 50%
    they are half way through leveling up.

    Used to animate progressbar as it needs to be full at 100%*/
    public void setPercentage() {

        double tmpMin = (double)minimumXP;
        double tmpMax = (double)maximumXP;
        double tmpOnLevel = (double)xpOnLevel;
        double tmp = (tmpOnLevel/(tmpMax - tmpMin))*100;
        this.percentage = (int)tmp;
        Log.d(TAG, "setPercentage:" + tmp);
    }

    public int getPercentage(){
        setPercentage();
        return percentage;
    }
}