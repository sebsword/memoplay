package com.example.sebsw.memoplay.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.model.Word;

/**
 * Created by sebsw on 08/11/2016.
 */

public class WordViewHolder extends RecyclerView.ViewHolder {
    private TextView wordLabel;
    public TextView wordOptionsButton;

    public WordViewHolder(View itemView) {
        super(itemView);
        this.wordLabel = (TextView)itemView.findViewById(R.id.wordLabel);
        this.wordOptionsButton = (TextView)itemView.findViewById(R.id.wordOptionsButton);
    }

    public void UpdateUI(Word word){
        String wordLabelText = word.get_word();
        wordLabel.setText(wordLabelText);
    }
}
