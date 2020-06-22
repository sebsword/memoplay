package com.example.sebsw.memoplay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.adapters.WordsAdapter;
import com.example.sebsw.memoplay.model.Category;
import com.example.sebsw.memoplay.services.CategoryDataService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mPosition;
    private String mParam2;


    public WordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position The position of the category that was clicked on (within the categories array
     *                to give us our currently opened list of words).
     //* @param param2 Parameter 2.
     * @return A new instance of fragment WordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance(int position) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_words, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.words_recycler);
        recyclerView.setHasFixedSize(true);

        WordsAdapter adapter;

        ArrayList<Category> possibleCategories = CategoryDataService.getInstance().loadAllCategories();

        adapter = new WordsAdapter(CategoryDataService.getInstance().loadCategoryWords(possibleCategories.get(mPosition)),mPosition);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return v;
    }
}
