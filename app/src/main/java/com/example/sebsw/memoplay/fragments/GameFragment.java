package com.example.sebsw.memoplay.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.model.Category;
import com.example.sebsw.memoplay.model.Score;
import com.example.sebsw.memoplay.model.Word;
import com.example.sebsw.memoplay.services.CategoryDataService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Manifest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";
    private static final String ARG_PARAM2 = "param2";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    // TODO: Rename and change types of parameters
    private int mPosition;
    private int mOldPostion;

    private Chronometer chronometer;
    private Word originWord;

    private int correctCharacters;

    private String mParam2;
    EditText definitionTest;
    Button refreshBtn, resetLevelBtn;
    TextView gameDisplay, wordLabel;
    boolean spaceBarPressed;

    private View dialogView;

    //stuff for popup dialog
//    private ConstraintLayout constraint_game;
//    private PopupWindow popupWindow;
//    private LayoutInflater layoutInflater;
    Button replayBtn, reviewBtn, shareScoreBtn;
    TextView scoreLabel;



    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //retrieves fragment arguments and stores them as variables.
            mPosition = getArguments().getInt(ARG_POSITION);
            mOldPostion = getArguments().getInt("old_position");
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        // Inflate the layout for this fragment

        correctCharacters = 0;

        //constraint_game = (ConstraintLayout)v.findViewById(R.id.constraint_game);

        // loads an arraylist all of the categories from the database

        /* This part was slightly difficult to implement because communication between different fragments
        in android is not as developed as communication between activities, in order for the game fragment
        to know which word to load it would need to load it also had to know which category that word belonged
        to, this meant finding a way to backtrack all the way to the original categories fragment and retrieving
        this information that was then passed to the game fragment as arguments during its creation, another issue
        then arised as only simple data types can be passed in as paramaters to a fragment, so I opted to pass the position
        of the word and category clicked on and then use these positions to locate the corresponding word and category.
         */
        ArrayList<Category> possibleCategories = CategoryDataService.getInstance().loadAllCategories();
        // selects the category needed to generate the game based on the position inputted as an argument
        final Category originCategory = possibleCategories.get(mOldPostion);
        ArrayList<Word> possibleWords = CategoryDataService.getInstance().loadCategoryWords(originCategory);
        originWord = possibleWords.get(mPosition);
        resetLevelBtn = (Button)v.findViewById(R.id.resetLevelBtn);
        resetLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Instantiating custom object, score
                Score score = new Score(originWord.get_id());
                //removes all progress on a current word
                score.resetScore();
                resetGame();
            }
        });

        wordLabel = (TextView)v.findViewById(R.id.wordLabel);
        wordLabel.setText(originWord.get_word());


        chronometer = (Chronometer)v.findViewById(R.id.Time_chronometer);

        gameDisplay = (TextView)v.findViewById(R.id.gameDisplay);
        originWord.reset();
        gameDisplay.setText(originWord.printDefinitionArray());

        refreshBtn = (Button)v.findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                legacy - old code
                -----------------------------
                originWord.reset();
                correctCharacters = 0;
                gameDisplay.setText(originWord.printDefinitionArray());
                definitionTest.setText("");
                definitionTest.setEnabled(true);
                chronometer.stop();
                chronometer.setText("00:00");
                -----------------------------
                 */
                resetGame();
            }
        });

        definitionTest = (EditText)v.findViewById(R.id.definintionTest);
        definitionTest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if the old text is less than the new text i.e if a character was added and not removed
                if(before < s.length()){
                    char last = s.charAt(s.length()-1);
                    // if the last character entered was a space
                    if(last == ' '){

                        Log.d("TAG","spacebar was pressed");
                        spaceBarPressed = true;
                    }
                }
                /*
                ensures that when the reset button is pressed the chronometer
                does not start, because the empty string value is not great enough
                for the if statement to be true
                 */
                if ((!originWord.isStarted()) && (s.length() >= 1)){
                    //boolean has started keeps track if the game has started for a particular word
                    originWord.setHasStarted(true);
                    Log.d("tag",Integer.toString(s.length()));
                    Log.d("tag","starting the chronometer");
                    //starts game timer
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (spaceBarPressed){
                    /* if the spacebar was indeed pressed, the last character of the word
                    now needs to be removed in order for the word to be processed further
                    because the last character would just be a blank space
                     */
                    String enteredWord = s.toString().substring(0,s.length()-1);
                    String correctWord = originWord.getSelectedWord();
                    if(enteredWord.equals(correctWord)){
                        originWord.setPrvWordState("correct");
                        // resets spaceBarPressed so game can continue
                        spaceBarPressed = false;
                        correctCharacters += correctWord.length();
                    }
                    else{
                        originWord.setPrvWordState("incorrect");
                        // resets spaceBarPressed so game can continue
                        spaceBarPressed = false;
                    }

                    originWord.incrementCurrentWordPosition();
                    updateGameDisplay(originWord);
                    //if originWord has no more characters to type
                    if (originWord.checkIfFinished()){
                        //stop timer
                        chronometer.stop();
                        definitionTest.setEnabled(false);
                        definitionTest.getBackground().setAlpha(128);
                        gameDisplay.setText("");
                        double timeElapsed = getElapsedTime(chronometer);
                        long wpm = calculateWordsPerMinute(correctCharacters, timeElapsed);
                        // need to generate xp earned

                        int xp = (int)((wpm * (correctCharacters / 5) )/10);
                        if (xp < 5){
                            xp = 5;
                        }
                        Score score = new Score(originWord.get_id(), xp);
                        score.updateScore();

                        // after game is done popup dialog launched
                        popupDialog(wpm);
                    }
                    definitionTest.setText("");
                }

            }
        });

        //some code here to attempt to set minimum height
        ViewTreeObserver viewTreeObserver = gameDisplay.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    gameDisplay.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int gameDisplayHeight = gameDisplay.getHeight();
                    gameDisplay.setMinHeight(gameDisplayHeight);
                    gameDisplay.setMinimumHeight(gameDisplayHeight);
                }
            });
        }

        return v;
    }

    private void updateGameDisplay(Word word){
        Spannable wordToSpan = new SpannableString(word.printDefinitionArray());
        switch (word.getPrvWordState()){
            //if the word was entered correctly set its colour in the game display to green
            case "correct":
                wordToSpan.setSpan(new ForegroundColorSpan(Color.GREEN),0,word.getPrvWord().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            //otherwise if it was entered incorrectly, set its colour in the display to red
            case "incorrect":
                wordToSpan.setSpan(new ForegroundColorSpan(Color.RED),0,word.getPrvWord().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            default:
                //nothing is done in this case
        }
        gameDisplay.setText(wordToSpan);


    }

    private double getElapsedTime(Chronometer chronometer){
        double elapsedMillis = (double)SystemClock.elapsedRealtime() - chronometer.getBase();
        return elapsedMillis / 1000;
    }

    private long calculateWordsPerMinute(int correctCharacters, double time){

        double timeInMinutes = time / 60;
        double correctCharactersD = (double) correctCharacters;
        double correctWords = correctCharactersD / 5;
        double wpm = correctWords / timeInMinutes;


        Log.d("TAG", "calculateWordsPerMinute: " + Double.toString(wpm));

        return Math.round(wpm);
    }

    private void popupDialog(long score){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.getMainActivity().Thiscontext);
        dialogView = getLayoutInflater(null).inflate(R.layout.finished_popup_window, null);
        mBuilder.setView(dialogView);
        final AlertDialog alertDialog = mBuilder.create();

        reviewBtn = (Button)dialogView.findViewById(R.id.reviewBtn);
        replayBtn = (Button)dialogView.findViewById(R.id.replayBtn);
        shareScoreBtn = (Button)dialogView.findViewById(R.id.shareScoreBtn);
        scoreLabel = (TextView)dialogView.findViewById(R.id.scoreLabel);

        String scoreString = Long.toString(score);
        scoreLabel.setText("You Scored: " + scoreString + " WPM" );
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code here to launch new fragment or maybe an activity
                MainActivity.getMainActivity().loadProfileFragment(originWord.get_id(), originWord.get_word());
                alertDialog.dismiss();
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                alertDialog.dismiss();

                // call refresh function and close dialog
            }
        });

        shareScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWritePermissions();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout(1300,1200);

    }

    private void resetGame(){
        originWord.reset();
        correctCharacters = 0;
        gameDisplay.setText(originWord.printDefinitionArray());
        definitionTest.setText("");
        definitionTest.setEnabled(true);
        definitionTest.getBackground().setAlpha(255);
        chronometer.stop();
        chronometer.setText("00:00");
    }

    private void captureScreen(){
        Date now = new Date();
        android.text.format.DateFormat.format("dd-MM-yyyy_hh:mm:ss",now);

        try{
            // name the image from current date and get storage location
            String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + now + ".jpg";

           /*
            Redundant code now
            View view = getActivity().getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
            Bitmap parentBitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            */

            // actually capturing the screen
            View mainView = getActivity().getWindow().getDecorView().getRootView();
            mainView.setDrawingCacheEnabled(true);
            Bitmap parentBitmap = Bitmap.createBitmap(mainView.getDrawingCache());
            mainView.setDrawingCacheEnabled(false);

            // create an array of integers called location with size 2
            int mainViewLocation[] = new int[2];
            mainView.getLocationOnScreen(mainViewLocation);

            // now the dialog view
            int dialogViewLocation[] = new int[2];
            dialogView.getLocationOnScreen(dialogViewLocation);

            dialogView.setDrawingCacheEnabled(true);
            Bitmap dialogBitmap = Bitmap.createBitmap(dialogView.getDrawingCache());
            dialogView.setDrawingCacheEnabled(false);

            Canvas canvas = new Canvas(parentBitmap);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            // makes the new paint a colour filter than darkens the resulting bitmap
            ColorFilter filter = new LightingColorFilter(0xFF999999, 0x00000000);
            paint.setColorFilter(filter);

            // draw the background view with the darkening colour filter applied
            canvas.drawBitmap(parentBitmap,mainViewLocation[0],mainViewLocation[1],paint);

            // draws the dialog view ontop of the main view now.
            canvas.drawBitmap(dialogBitmap,dialogViewLocation[0] - mainViewLocation[0],dialogViewLocation[1] - mainViewLocation[1],new Paint());

            //creating file at specified path
            File imageFile = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int imageQuality = 100;
            parentBitmap.compress(Bitmap.CompressFormat.PNG, imageQuality, outputStream);
            outputStream.flush();
            outputStream.close();

            shareImage(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //procedure for sharing the image file
    private void shareImage(File imageFile){
        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT,"Memoplay Results");
        intent.putExtra(Intent.EXTRA_TEXT,"I achieved this score using the Memoplay learning app, can you beat my score?");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        //launches an intent from the main activity to share the file
        MainActivity.getMainActivity().loadShareActivity(intent,"Share Score");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("TAG", Integer.toString(grantResults.length) + grantResults[0]);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (((grantResults.length > 0)&&(grantResults[0] == PackageManager.PERMISSION_GRANTED))){
                    // permission granted now you can check if the next one is grantged
                    requestReadPermissions();
                }else {
                    Toast.makeText(MainActivity.getMainActivity(),"Sorry but write permission has not been granted",Toast.LENGTH_LONG).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(((grantResults.length > 0)&&(grantResults[0] == PackageManager.PERMISSION_GRANTED))){
                    captureScreen();
                }else {
                    Toast.makeText(MainActivity.getMainActivity(),"Sorry, but read permission has not been granted",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void requestWritePermissions(){
        if ((ContextCompat.checkSelfPermission(MainActivity.getMainActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.getMainActivity(),new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            // the app then automatically checks if you accept
            // and if you have chosen to accept, it checks read permissions next
        }// the else means permission was already granted in which case we would need to check read permissions
        else {
            requestReadPermissions();
        }
    }

    private void requestReadPermissions(){
        if ((ContextCompat.checkSelfPermission(MainActivity.getMainActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE))!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.getMainActivity(), new  String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else {
            captureScreen();
        }
    }
}