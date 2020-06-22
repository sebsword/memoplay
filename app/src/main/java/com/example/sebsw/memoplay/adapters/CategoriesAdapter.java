package com.example.sebsw.memoplay.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.holders.CategoryViewHolder;
import com.example.sebsw.memoplay.model.Category;

import java.util.ArrayList;

/**
 * Created by sebsw on 27/10/2016.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private ArrayList<Category> categories;

    //this is the class constructor
    public CategoriesAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflates the card layout to create a new card view
        View categoryCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category,parent,false);
        return new CategoryViewHolder(categoryCard);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        /*there is an arrayList of all the categories
        when a viewholder is set the information about
        a particular category at a particular position
        in the arraylist is grabbed so that it can be
        turned into a view
         */
        final Category category = categories.get(position);
        // updates the viewholder with the information for a category at a particular position in the array
        holder.updateUI(category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load details activity
                // passes in the position of the category that was clicked on
                // so we know which category to load
                MainActivity.getMainActivity().loadNewWordsFragment(position);
            }
        });
    }

    public interface PassExtra{
        public void onCategorySelected(int position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
