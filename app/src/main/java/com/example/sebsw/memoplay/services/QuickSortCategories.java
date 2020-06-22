package com.example.sebsw.memoplay.services;

import android.util.Log;

import com.example.sebsw.memoplay.model.Category;

import java.util.ArrayList;

/**
 * Created by sebsw on 10/04/2017.
 */

public class QuickSortCategories {
    private ArrayList<Category> categories;

    private int size;

    public void sort(ArrayList<Category> values){
        // checking to see if  array list is empty
        if (values == null || values.size()==0){
            return;
        }
        else {
            this.categories = values;
            size = values.size();
            Log.d("TAG", "after sort: " + this.categories);
            quickSort(0, size - 1);
            Log.d("TAG", "after sort: " + this.categories);
        }

    }

    private void  quickSort(int low, int high){
        int i = low;
        int j = high;

        String pivotTitle = categories.get(low + (high-low)/2).getCategoryTitle();

        while (i<=j){
            //compares the value of the letter at index i of the arrayList
            while (compareAZ(i, 0, pivotTitle, 0) <= -1){
                i++;
            }

            /*moves down the right side of the list until a number is found
            that is less than the pivot
             */
            while (compareAZ(j, 0, pivotTitle, 0) >= 1 ){
                j--;
            }

            // if an element in the left list is less than the pivot
            //and an element
            if (i <= j){
                exchange(i,j);
                i++;
                j--;
            }
        }
        // recursion
        if (low < j){
            quickSort(low, j);
        }
        if (i < high){
            quickSort(i, high);
        }

    }

    private void exchange(int i, int j){
        Category temp = categories.get(i);
        categories.set(i, categories.get(j));
        categories.set(j, temp);

    }

    private int compareAZ(int i, int letterIndex, String pivotTitle, int pLetterIndex){

        Character cC= categories.get(i).getCategoryTitle().charAt(letterIndex);
        Character pC = pivotTitle.charAt(pLetterIndex);

        // if the letters are not the same
        //base case
        if (cC.compareTo(pC) != 0){
            return cC.compareTo(pC);
        /* essentially before moving onto the next letter we need to check if
           if there are any more letters to move onto
            */
        //alternative case - out of characters to compare, words are essentially equal
        }else if ((letterIndex == categories.get(i).getCategoryTitle().length()-1)||(pLetterIndex == pivotTitle.length()-1)){
            return 0;
        }else {
            /* recursively call the function comparing the next
            letter of the the category to the next letter in the pivot
             */
            compareAZ(i, letterIndex + 1, pivotTitle, pLetterIndex + 1);
        }
        // should never be called
        return 0;
    }
}
