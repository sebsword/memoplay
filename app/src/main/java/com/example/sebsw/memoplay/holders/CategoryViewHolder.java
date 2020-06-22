package com.example.sebsw.memoplay.holders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.fragments.CategoriesFragment;
import com.example.sebsw.memoplay.model.Category;

/**
 * Created by sebsw on 27/10/2016.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private ImageView mainImage;
    private TextView titleTextView;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        // allows the traits of each category to be bound to the card view
        this.mainImage = (ImageView)itemView.findViewById(R.id.main_image);
        this.titleTextView = (TextView)itemView.findViewById(R.id.main_text);
    }
    /*
    this just makes a generic category,
    when this method is called within the adapter class
    its picture location and title will then be bound to the view
     */
    public void updateUI(Category category){
        String uri = category.getImgUri();
        //int resource = mainImage.getResources().getIdentifier(uri, null,mainImage.getContext().getPackageName());
       // mainImage.setImageResource(resource);
        Log.d("DOGODA", uri);
        Glide.with(itemView.getContext()).load(mainImage.getResources().getIdentifier(uri, null,mainImage.getContext().getPackageName())).placeholder(R.drawable.default_banner).into(mainImage);


        titleTextView.setText(category.getCategoryTitle());
    }
}
