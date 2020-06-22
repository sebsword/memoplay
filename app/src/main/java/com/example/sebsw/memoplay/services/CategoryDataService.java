package com.example.sebsw.memoplay.services;

import android.content.Context;
import android.provider.UserDictionary;

import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.model.Category;
import com.example.sebsw.memoplay.model.Word;

import java.util.ArrayList;

/**
 * Created by sebsw on 28/10/2016.
 */
public class CategoryDataService {
    private static CategoryDataService ourInstance = new CategoryDataService();

    public static CategoryDataService getInstance() {
        return ourInstance;
    }

    private CategoryDataService() {
    }

    public ArrayList<Category> loadAllCategories(){
        Context context = MainActivity.getMainActivity().getApplicationContext();
        ArrayList<Category> list;
        MyDBHandler myDBHandler = new MyDBHandler(context,null,null,1);
        list = myDBHandler.retrieveCategories();


        /*
        old static example

        ArrayList<Category> list = new ArrayList<>();
        list.add(new Category("AQA GCE Biology","biology_banner"));
        list.add(new Category("AQA GCE Computer Science","comp_banner"));
        list.add(new Category("OCR GCE Physics", "physics_banner"));
        */

        return list;
    }

    public ArrayList<Word> loadCategoryWords(Category category){
        Context context = MainActivity.getMainActivity().getApplicationContext();
        ArrayList<Word> words;
        MyDBHandler myDBHandler = new MyDBHandler(context,null,null,1);
        words = myDBHandler.retrieveCategoriesWords(category);
        return words;
    }


}

