package com.example.sebsw.memoplay.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sebsw.memoplay.fragments.CategoriesFragment;
import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.fragments.GameFragment;
import com.example.sebsw.memoplay.fragments.ImportFragment;
import com.example.sebsw.memoplay.fragments.NewDefinitionFragment;
import com.example.sebsw.memoplay.fragments.ProfileFragment;
import com.example.sebsw.memoplay.fragments.WordsFragment;
import com.example.sebsw.memoplay.services.BackLoad;
import com.example.sebsw.memoplay.services.MyDBHandler;

public class MainActivity extends AppCompatActivity {

    /* This constant is defined so that a named constant can be put into a bundle when creating a new fragment,
    this constant is the position of a category within the main list of categories.
     */
    private static final String ARG_POSITION = "position";

    public static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    public Context Thiscontext = MainActivity.this;

    //This gives a name to the mainActivity so it can be referenced
    private static MainActivity mainActivity;

    /*
    Here is a function that gets the mainActivity so
    that its other methods can be called from other packages
    in my code
     */
    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    /*Every activity has a method called onCreate that is called when the activity
    is launched, overriding this allows us to control what happens when the activity is launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.setMainActivity(this);

        // logs are used throughout the code for  debugging purposes, they help me keep track of changes in the code
        // and allow me to see which parts of the code are functioning and being called as intended
        // they can safely be ignored when looking through the code
        Log.d("testing123","testing to see if logs actuaally do anything");

        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        dbHandler.addImages();

        // a fragment manager is needed to manage which fragments will be displayed on the screen.
        FragmentManager fm = getSupportFragmentManager();
        // Creating an instance of the categories fragment
        CategoriesFragment categoriesFragment = (CategoriesFragment)fm.findFragmentById(R.id.container_main);

        /*
        if the app is first opening the categories fragment
        will be null as it has not yet been launched
         */
        if (categoriesFragment == null){
            categoriesFragment = categoriesFragment.newInstance("Param1","Param2");
            fm.beginTransaction().add(R.id.container_main,categoriesFragment).commit();

        }
        Log.d("TAG", getIntent().getAction().toString());
        /* Different intents describe the method used to launch the application
        The code below is used to capture the intent, so we know the reason why
        the application has been launched and can handle this appropriately.

        In this particular example if the intent is to view something the code is ran
        View is the intent launched when a browser link is clicked on, the link can then be further
        processed to extract useful information
         */
        if(getIntent().getAction().equals("android.intent.action.VIEW")){
            Log.d("TAG", "browsable found");
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            /*
            The id of a word stored in the database is retrieved, this is used to pass into the function backload
            that will download the word based on the id provided
             */
            String id = data.getQueryParameter("id");
            Log.d("TAG", "the id of the link clicked is " + id);
            BackLoad backLoad = new BackLoad();
            /* backLoad is called with the 'method' import, letting class know that it will be importing a word
            from the online database with the id passed as the second parameter.
             */
            backLoad.execute("import",id);

        }
    }

    /*
    Since we are using fragments there will only be one Activity
    and it is therefore preferable to load all new fragments
    from methods on the mainActivity

    e.g. when I need to launch the game fragment, I simply call the corresponding method on the main activity
    This is useful as it helps repeating code, many other fragments may need to also launch the game fragment
    but they can all do it now by accessing the main activity.

    the functions below are all public so they can be called to launch the corresponding fragments
    when needed by the program, usually as a response to a user clicking a button.
     */

    public void loadNewDefinitionScreen(){
        // replaces the current fragment on the screen with the fragment to create new definitions
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NewDefinitionFragment()).addToBackStack(null).commit();
    }

    public void loadNewWordsFragment(int position){
        //replaces the current fragment on the screen with the fragment containing a list of all words

        //WordsFragment wordsFragment = (WordsFragment)getSupportFragmentManager().findFragmentById(R.id.container_main);
        Bundle parameters = new Bundle();
        parameters.putInt(ARG_POSITION, position);
        WordsFragment wordsFragment = new WordsFragment();
        wordsFragment.setArguments(parameters);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, wordsFragment).addToBackStack(null).commit();
    }

    public void loadGameActivity(int position, int oldPosition){
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt("old_position", oldPosition);
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(args);

        // replaces the current fragment on the screen with the fragment to play the main game
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, gameFragment).addToBackStack(null).commit();
    }

    public void loadProfileFragment(int wordId, String wordTitle){
        Bundle args = new Bundle();
        args.putInt("word_id",wordId);
        args.putString("word_title",wordTitle);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(args);

        // replaces the current fragment on the screen with the fragment to display users gain in experience
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, profileFragment).addToBackStack(null).commit();
    }

    public void loadCategoriesFragment(){
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        // displays on screen the fragment containing list of all categories
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, categoriesFragment).commit();
    }

    public void loadImportFragment(){
        ImportFragment importFragment = new ImportFragment();
        //launches fragment where user can import words and definitions from the online database.
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, importFragment).addToBackStack(null).commit();
    }

    public void loadShareActivity(Intent intent, String description){
        // launches an intent to share an image with other users, default android share intent
        startActivity(Intent.createChooser(intent,description));
    }

}
