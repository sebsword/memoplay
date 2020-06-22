package com.example.sebsw.memoplay.model;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by sebsw on 31/10/2016.
 */

public class Word {

    public String TAG = "TAG";

    private int _id;
    private String _word;
    private String _category;
    private String _definition;

    private Score score;
    private int wordsToSkip;

    //class constructor
    public Word(String _word, String _category, String _definition) {
        this.set_word(_word);
        this.set_category(_category);
        this.set_definition(_definition);
    }

    /*Constructor made using method overloading, word ID
    is supplied as an additional parameter */
    public Word(int _id, String _word, String _category, String _definition) {
        this.set_word(_word);
        this.set_category(_category);
        this.set_definition(_definition);
        this.set_id(_id);
    }

    //getters and setters
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_word() {
        return _word;
    }

    public void set_word(String _word) {
        this._word = _word;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public String get_definition() {
        return _definition;
    }

    public void set_definition(String _definition) {
        this._definition = _definition;
    }

    //turns  the definition into an arrayList of strings that the game gan be played woth
    public void setDefinitonArray(){
        String[] temp  = _definition.split(" ");
        this.definitionArray = new ArrayList<String>(Arrays.asList(temp));
    }

    private ArrayList<String> definitionArray;
    private ArrayList<String> defBufferArray;

    public ArrayList<String> getDefinitionArray(){
        return definitionArray;
    }

    /* creates the underbar needed to hide a word in the definition
    redaction needs to be as long as the original length of the word
    user must know length of unknown word in order to guess it correctly*/
    public String underbar(int wordLength){
        String tmpUnderbar = "";

        //add an underscore for each letter in the word
        for (int i = 0; i<= wordLength; i++){
            tmpUnderbar += "_";
        }
        return tmpUnderbar;
    }

    //finds the position of each word that is 3 or greater characters long
    public ArrayList<Integer> positionOf3CharWords(){
        ArrayList<Integer> positions = new ArrayList<>();
        int i = 0;
        for (String s : definitionArray){
            if (s.length() >= 3){
                positions.add(i);
            }
            i++;
        }return positions;
    }

    /* Replaces a random selection of 3 letter words with underbars, stores new
     * result results in a buffer array, that once formatted can be displayed
     * to the user when they are playing the game*/
    public void setDefBufferArray(){
        ArrayList<String> tmpBuffer = new ArrayList<>();
        tmpBuffer.clear();
        tmpBuffer.addAll(definitionArray);
        Log.d(TAG, "setDefBufferArray: "+ tmpBuffer);
        Log.d(TAG, "Will now skip" + wordsToSkip);
        Random random = new Random();
        for (int i = 0; i < getWordsToSkip(); i++) {
            int randInt = random.nextInt(count3LetterWords(definitionArray));
            Log.d(TAG, "setDefBufferArray: the random integer selected is " + randInt);
            int indexToChange = positionOf3CharWords().get(randInt);
            Log.d(TAG, "setDefBufferArray: setting " + definitionArray.get(indexToChange) + " to " + underbar(definitionArray.get(indexToChange).length()));
            tmpBuffer.set(indexToChange, underbar(definitionArray.get(indexToChange).length()));
            Log.d(TAG, "The value of i is now " + i + " which is <=" + wordsToSkip);
        }
        defBufferArray = tmpBuffer;

    }
    public  ArrayList<String>getDefBufferArray(){return defBufferArray;}

    private boolean started =  false;

    public void setHasStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    // returns the word at the front of the array that the user will be attempting to type
    public String getSelectedWord (){
        return definitionArray.get(0);
    }

    public boolean checkIfFinished(){
        if (definitionArray.isEmpty()){
            Log.d("Empty","there are no more words to type");
            return true;
        }
        else {
            return  false;
        }
    }

    public String printDefinitionArray(){
        String tmp = "";
        //adds spaces to the words in the definitionBufferArray so they can be printed
        for (String s : defBufferArray){
            tmp += s + " ";
        }
        /* if the game hasn't started, there is no previous word so we just print
        the array as it is */
        if (prvWord.equals("")){
            return tmp;
        }else{
            /* otherwise we must print the previous word, in front of the rest of the definition
            this way the user knows if they had entered it correctly
             */
            return prvWord + " " + tmp;
        }

    }

    private String prvWord = "";

    private int count3LetterWords(ArrayList<String> array){
        int count = 0;
        for (String s :  array){
            if (s.length() >= 3){
                count += 1;
            }
        }
        return count;
    }

    public void  setScore(){this.score = new Score(_id);}
    public void setWordsToSkip(){
        setScore();
        int tmpWordsToSkip = score.getLevel() -2;
        if (tmpWordsToSkip >= definitionArray.size()) {
            Log.d(TAG, tmpWordsToSkip + " was greater than " + definitionArray.size());
            tmpWordsToSkip = definitionArray.size();
            Log.d(TAG, "setWordsToSkip now becomes" + tmpWordsToSkip);
            wordsToSkip = tmpWordsToSkip;
        }else {
            wordsToSkip = tmpWordsToSkip;
        }
        Log.d(TAG, "setWordsToSkip:" + wordsToSkip);
    }
    public int getWordsToSkip(){
        return wordsToSkip;
    }



    public String getPrvWord(){
        return prvWord;
    }

    private String prvWordState;

    // attribute that stores whether the last word was entered correctly or not
    public void setPrvWordState(String s){
        this.prvWordState = s;
    }

    //public getter for previous word state attribute
    public String getPrvWordState(){
        return  prvWordState;
    }

    public void incrementCurrentWordPosition(){
        /*checks to see if the current word is redacted / hidden
        as if it is, the definition array will not equal the buffer array, which
        stores the words in their redacted format */
        if ( definitionArray.get(0).equalsIgnoreCase(defBufferArray.get(0)) ){
            this.prvWord = definitionArray.get(0);
            Log.d(TAG, "definiton arrays equal, removing" + definitionArray.get(0) + definitionArray.get(0));
            definitionArray.remove(0);
            defBufferArray.remove(0);

        }
        else {
            this.prvWord = defBufferArray.get(0);
            Log.d(TAG, "definiton arrays not equal, removing" + definitionArray.get(0) + definitionArray.get(0));
            definitionArray.remove(0);
            defBufferArray.remove(0);
        }
    }

    public void reset(){
        setDefinitonArray();
        setWordsToSkip();
        setDefBufferArray();
        setHasStarted(false);
        this.prvWord = "";
    }
}
