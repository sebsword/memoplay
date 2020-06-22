package com.example.sebsw.memoplay.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sebsw.memoplay.model.Category;
import com.example.sebsw.memoplay.model.Word;

import java.util.ArrayList;

/**
 * Created by sebsw on 30/10/2016.
 */

public class MyDBHandler extends SQLiteOpenHelper{
    /*
    Here I declare certain variables important variables
    mainly so that they do not need to passed in the class
    constructor (since I already know them, there is no point
    putting them in the class constructor I can just declare
    them here) This is my Table SCHEMA
     */

    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "Memoplay.db";

    /*
    The outline of three interlinked tables is shown in this schema
    two or three interlinked tables falls within the Group B section of technical
    Solution.
     */

    public static final String TABLE_NAME_WORDS = "words";
    public static final String COLUMN_WORD_ID = "word_id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_CATEGORY = "categorytitle";
    public static final String COLUMN_DEFINITION = "definiton";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_XP = "experience_points";


    public static final String TABLE_NAME_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    //public static final String COLUMN_CATEGORY = "categorytitle";
    public static final String COLUMN_IMGURI = "imguri";

    public static final String TABLE_NAME_IMAGES = "images";
    public static final String COLUMN_IMAGE_ID = "image_id";
    public static final String COLUMN_IMAGE_NAME = "image_name";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_IMAGE_TAGS = "image_tags";

    //constructor method
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createWordsTableQuery = "CREATE TABLE " +
                TABLE_NAME_WORDS + "(" +
                COLUMN_WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORD + " TEXT, " +
                COLUMN_DEFINITION + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_LEVEL + " INTEGER, " +
                COLUMN_XP + " INTEGER  " + ");";
        db.execSQL(createWordsTableQuery);

        String createCategoriesTable = "CREATE TABLE " +
                TABLE_NAME_CATEGORIES + "(" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_IMGURI + " TEXT" + ");";
        db.execSQL(createCategoriesTable);

        String createImgTable = "CREATE TABLE " + TABLE_NAME_IMAGES + "(" +
                COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_NAME + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT, " +
                COLUMN_IMAGE_TAGS + " TEXT" + ");";
        db.execSQL(createImgTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMAGES);
        onCreate(db);
    }

    public void addWord(Word word){
        ContentValues values = new ContentValues();
        //empty set called contentvalues that takes key and value parameters
        values.put(COLUMN_WORD, word.get_word());
        values.put(COLUMN_CATEGORY, word.get_category());
        values.put(COLUMN_DEFINITION, word.get_definition());
        values.put(COLUMN_XP, 0);
        //gets the database
        SQLiteDatabase db = getWritableDatabase();
        // inserts data into table , null , (columnName , value)
        db.insert(TABLE_NAME_WORDS,null,values);
        //closes the database afterwords for saftey
        db.close();
    }

    public void addImages(){
        /*
        Multidimentional array containing info about each image in the format:
        ImageName, ImagePath URI, Image Tags
         */
        String allImages[][] = {
                {"AQA GCE Biology","biology_banner","Biology"},
                {"AQA GCE Computer Science","comp_banner","Computer"},
                {"OCR GCE Physics","physics_banner","Physics"},
                {"Robotics Banner 1","robotics_banner","Robotics"},
                {"Economics Banner 1","economics_banner","Economics"}
        };
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        for (String[] a : allImages){
            values.clear();
            /*
            Puts the data stored on each Image into the images table
            correctly matching list indexes list with column names in database
             */
            values.put(COLUMN_IMAGE_NAME,a[0]);
            values.put(COLUMN_IMAGE_PATH,a[1]);
            Log.d("imagePath",a[2]);
            values.put(COLUMN_IMAGE_TAGS,a[2]);
            Log.d("allValuestoBeInserted",values.toString());
            db.insert(TABLE_NAME_IMAGES,null,values);
        }
        Log.d("Testing","testing to see if add images is ever even run");
    }

    public String selectImages(String categoryName){
        int tmpId;

        String tmpImgName;
        String imgNameArray[];

        String tmpImgPath;

        String tmpTags;
        String tagArray[];

        //assigns the default imgURI incase no matching image is found in the database
        String finalImagePath = "default_banner";

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_IMAGES + " WHERE 1";

        Cursor d = db.rawQuery(query,null);
        Log.d("Testing123456", "I am about to move the cursor to the beggining");
        d.moveToFirst();
        Log.d("Testing12345678",d.getString(d.getColumnIndex(COLUMN_IMAGE_TAGS)));
        while (!d.isAfterLast()){
            if(d.getString(d.getColumnIndex(COLUMN_IMAGE_PATH))!= null){
                tmpImgName = categoryName;
                tmpId = d.getInt(d.getColumnIndex(COLUMN_IMAGE_ID));
                tmpImgPath = d.getString(d.getColumnIndex(COLUMN_IMAGE_PATH));
                tmpTags = d.getString(d.getColumnIndex(COLUMN_IMAGE_TAGS));
                Boolean found = false;
                tagArray = tmpTags.split(" ");
                imgNameArray = tmpImgName.split(" ");
                for (String s : tagArray){
                    for (String ss : imgNameArray){
                        if (s.equalsIgnoreCase(ss)){
                            found = true;
                            String foundResult = s + "=" + ss;
                            Log.d("testing1234", foundResult);
                        }
                        else {
                            String foundResult =  s + "!=" + ss;
                            Log.d("testing12345", foundResult);
                            //move to next part of the code
                    }
                }
                if (found){
                    finalImagePath = tmpImgPath;
                    d.moveToLast();
                    d.moveToNext();
                }

                }
            }d.moveToNext();
        }//db.close();
        Log.d("testing","if this runs then the entire while loop is never true which would piss me off");
        return finalImagePath;
    }


    public void addCategory(){
        ArrayList<String> dbArray = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        /*Populates categories table, from all of the unique categories in the
        words table, that do not yet exist within the categories table */
        String query = "SELECT DISTINCT " + COLUMN_CATEGORY + " FROM " +
                TABLE_NAME_WORDS + " WHERE "+ COLUMN_CATEGORY +
                " NOT IN" + "(" + " SELECT " + COLUMN_CATEGORY + " FROM " + TABLE_NAME_CATEGORIES +
                " WHERE 1 " + ");" ;
        //"(" + " SELECT " + TABLE_NAME_CATEGORIES + "." + COLUMN_CATEGORY +
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){

            if(c.getString(c.getColumnIndex(COLUMN_CATEGORY))!= null){
                /*after we have made sure that the current value is
                not empty and have added it to our array, we then
                move to the next record */
                dbArray.add(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
            }

            c.moveToNext();
        }c.close();
        ContentValues values = new ContentValues();
        for (int i = 0; i < dbArray.size() ; i++){
            String currentCategory = dbArray.get(i);
            String imgPath = selectImages(currentCategory);
            values.put(COLUMN_CATEGORY, currentCategory);
            values.put(COLUMN_IMGURI, imgPath);
            db.insert(TABLE_NAME_CATEGORIES, null, values);
        }
        db.close();
    }

    public ArrayList<Category> retrieveCategories(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Category> dbArrayList = new ArrayList<>();
        String row;
        String query = "SELECT * FROM " + TABLE_NAME_CATEGORIES + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_CATEGORY))!= null){
                String category = c.getString(c.getColumnIndex(COLUMN_CATEGORY));
                String imgUri = c.getString(c.getColumnIndex(COLUMN_IMGURI));
                dbArrayList.add(new Category(category,imgUri));
            }
            c.moveToNext();
        }
        db.close();
      return dbArrayList;
    }

    public ArrayList<Word> retrieveCategoriesWords(Category category){
        String categoryName = category.getCategoryTitle();
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Word> wordArrayList = new ArrayList<>();
        String queryMatchingCategory = "SELECT * FROM " + TABLE_NAME_WORDS +
                " WHERE " + COLUMN_CATEGORY + " = ? ";
        Cursor c = db.rawQuery(queryMatchingCategory, new String[] {categoryName});
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_WORD))!= null){
                int wordId = c.getInt(c.getColumnIndex(COLUMN_WORD_ID));
                String wordName = c.getString(c.getColumnIndex(COLUMN_WORD));
                String wordDefinition = c.getString(c.getColumnIndex(COLUMN_DEFINITION));
                wordArrayList.add(new Word(wordId,wordName, categoryName, wordDefinition));
            }c.moveToNext();
        }db.close();
        return wordArrayList;
    }

    public void updateScore(int id ,int changeInlevel, int XPtoAdd){
        SQLiteDatabase db = getWritableDatabase();
        /*
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LEVEL,level);
        contentValues.put(COLUMN_XP,totalXP);
        db.update(TABLE_NAME_WORDS, contentValues, COLUMN_WORD_ID + " = " + id, null);
        */

        String updateQuery = "UPDATE " + TABLE_NAME_WORDS + " SET " + COLUMN_LEVEL + " = " + COLUMN_LEVEL + "+ ? , " +
                COLUMN_XP + " = " + COLUMN_XP + "+ ? WHERE " + COLUMN_WORD_ID + " = ? ;";
        db.execSQL(updateQuery, new String[] {Integer.toString(changeInlevel), Integer.toString(XPtoAdd), Integer.toString(id) });
        db.close();
    }

    public void resetScore(int id){
        SQLiteDatabase db = getWritableDatabase();
        /*
        Redundant Code
        --------------------------------------------
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LEVEL,level);
        contentValues.put(COLUMN_XP,totalXP);
        db.update(TABLE_NAME_WORDS, contentValues, COLUMN_WORD_ID + " = " + id, null);
        --------------------------------------------
        */

        //resets the level to zero for a word with a specific ID
        String updateQuery = "UPDATE " + TABLE_NAME_WORDS + " SET " + COLUMN_LEVEL + " = ? , " +
                COLUMN_XP + " = ? WHERE " + COLUMN_WORD_ID + " = ? ;";
        db.execSQL(updateQuery, new String[] {"0", "0", Integer.toString(id)});
        db.close();
    }

    public int getTotalXP(int id){
        int level = 0;
        int totalXP = 0;
        SQLiteDatabase db = getWritableDatabase();
        String XpQuery = " SELECT " + COLUMN_LEVEL +", " + COLUMN_XP + " FROM " + TABLE_NAME_WORDS + " WHERE " + COLUMN_WORD_ID + " = ? ";
        Cursor c = db.rawQuery(XpQuery, new String[] {Integer.toString(id)});
        c.moveToFirst();
        
        while (!c.isAfterLast()){
            Log.d("TAG", "getTotalXP: test to see if c is ever after last");
            if (c.getString(c.getColumnIndex(COLUMN_XP))!=null){
                level = c.getInt(c.getColumnIndex(COLUMN_LEVEL));
                totalXP = c.getInt(c.getColumnIndex(COLUMN_XP));
                Log.d("TAG","getTotalXP: jaja " + totalXP);
            }c.moveToNext();
        }db.close();
        // index 0 = lvl index 1 = totalXP
        Log.d("TAG","getTotalXP: " + totalXP);
        //only returning totalXP as the level can easily be re-calculated
        return totalXP;
    }

    public void deleteWord(String wordTitle, String category){
        SQLiteDatabase db = getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME_WORDS + " WHERE " + COLUMN_WORD + " = ? AND "
                + COLUMN_CATEGORY + " = ?";
        db.execSQL(deleteQuery, new String[] {wordTitle, category});
        if (checkCategoryEmpty(category)){
            Log.d("TAG", "the category was empty and " + category + " should be deleted now");
            /* here I will be testing a parametrised query using the question mark
            this helps to prevent sql injections and is also neater and has many other benefits
            than hardcoding using concatenation6
             */

            //if the category is empty, then we also delete the category
            String deleteCatQuery = "DELETE FROM " + TABLE_NAME_CATEGORIES + " WHERE " +
                    COLUMN_CATEGORY + " = ? ";
            db.execSQL(deleteCatQuery, new String[] {category});
        }
        db.close();
    }

    public boolean checkCategoryEmpty(String category){
        SQLiteDatabase db = getWritableDatabase();
        int numberOfWords = 0;
        String query = "SELECT * FROM " + TABLE_NAME_WORDS + " WHERE " + COLUMN_CATEGORY + " = ? ";
        Cursor c = db.rawQuery(query, new String[] {category});
        c.moveToFirst();
        while (!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_WORD))!= null){
                numberOfWords += 1;
                Log.d("TAG", " a word was found in your category this word is" + c.getString(c.getColumnIndex(COLUMN_WORD)));
            }c.moveToNext();
        }
        if (numberOfWords == 0){
            //there are no words in the category, so it is empty
            return true;
        }else {
            Log.d("TAG", "a word was found and the total number of words is " + numberOfWords);
            return false;

        }
    }
}
