package com.example.sebsw.memoplay.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.holders.WordViewHolder;
import com.example.sebsw.memoplay.model.Category;
import com.example.sebsw.memoplay.model.Word;
import com.example.sebsw.memoplay.services.BackLoad;
import com.example.sebsw.memoplay.services.CategoryDataService;
import com.example.sebsw.memoplay.services.MyDBHandler;

import java.util.ArrayList;

/**
 * Created by sebsw on 08/11/2016.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordViewHolder>{
    private ArrayList<Word> words;
    private int oldPositionTest;
    private Context context;
    private MyDBHandler dbHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);

    public WordsAdapter(ArrayList<Word> words, int oldPositionTest) {
        this.words = words;
        this.oldPositionTest = oldPositionTest;
        this.context = context;
    }
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View wordCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_word,parent,false);
        return new WordViewHolder(wordCard);
    }

    @Override
    public void onBindViewHolder(final WordViewHolder holder, final int position) {
        final Word word = words.get(position);
        holder.UpdateUI(word);

        holder.wordOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show popup menu when the button is clicked
                PopupMenu popupMenu = new PopupMenu(MainActivity.getMainActivity(), holder.wordOptionsButton);
                popupMenu.inflate(R.menu.word_context_menu);
                // add the click listener for  each individual menu item
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //retrieve info about the word
                        String TAG = "input data";
                        String wordTitle = word.get_word();
                        Log.d(TAG, "word is" + wordTitle);
                        String wordDefinition = word.get_definition();
                        Log.d(TAG, "definition is" + wordDefinition);
                        String wordCategory = word.get_category();
                        Log.d(TAG, "category is" + wordCategory);

                        switch (item.getItemId()){
                            case R.id.shareWord:

                                // put this info into a background task
                                String method = "share";
                                BackLoad backload = new BackLoad();
                                backload.execute(method, wordTitle, wordDefinition, wordCategory );
                                break;
                            case R.id.deleteWord:
                                // deletes word at position
                                dbHandler.deleteWord(wordTitle,wordCategory);
                                MainActivity.getMainActivity().loadCategoriesFragment();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load gameActivity
                //int oldPosition = getSelectedCategory(word);

                /* old position stores the position of the category that was clicked on to load
                the current array adapter

                position stores the position of the word that was clicked on,  so the game activity
                knows which word to load
                 */
                MainActivity.getMainActivity().loadGameActivity(position, oldPositionTest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    private int getSelectedCategory(Word word){
        ArrayList tmp = CategoryDataService.getInstance().loadAllCategories();
        String CategoryName = word.get_category();
        Log.d("category", CategoryName);
        int oldPosition = tmp.indexOf(CategoryName);
        Log.d("position", Integer.toString(oldPosition));
        return oldPosition;
    }
}