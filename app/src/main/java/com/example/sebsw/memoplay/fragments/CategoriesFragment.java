package com.example.sebsw.memoplay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.adapters.CategoriesAdapter;
import com.example.sebsw.memoplay.model.Category;
import com.example.sebsw.memoplay.services.CategoryDataService;
import com.example.sebsw.memoplay.services.QuickSortCategories;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //my stuff
    Button addWordBtn;
    TextView categoryMenuBtn;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.categories_recycler);
        recyclerView.setHasFixedSize(true);
        final CategoriesAdapter adapter;

        //creating variables for the different buttons in the layout
        addWordBtn = (Button)v.findViewById(R.id.newWordBtn);

        addWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().loadNewDefinitionScreen();
            }
        });


        final ArrayList<Category> categories = CategoryDataService.getInstance().loadAllCategories();
        adapter = new CategoriesAdapter(categories);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

        categoryMenuBtn = (TextView) v.findViewById(R.id.categoryMenuBtn);
        categoryMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when the menu button is clicked a Popup option menu is created on top of the main activity
                 */
                PopupMenu popupMenu = new PopupMenu(MainActivity.getMainActivity(),categoryMenuBtn);
                popupMenu.inflate(R.menu.category_sort_menu);
                // adds the onclick listener for each item
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sortAZ:
                                // sort the menu a - z
                                QuickSortCategories quickSortCategories = new QuickSortCategories();
                                quickSortCategories.sort(categories);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "onMenuItemClick:" + categories);
                                break;
                            case R.id.sortZA:
                                //sort the menu z - a
                                QuickSortCategories quickSortCategoriesReverse = new QuickSortCategories();
                                //by sorting it a - z
                                quickSortCategoriesReverse.sort(categories);
                                // and then reversing it
                                Collections.reverse(categories);
                                // notifies adapter that dataset has changed so view is updated
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "onMenuItemClick:" + categories);

                                break;
                            default:
                                // do nothing
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        return v;


    }


}
