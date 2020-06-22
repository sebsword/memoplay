package com.example.sebsw.memoplay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.model.Word;
import com.example.sebsw.memoplay.services.MyDBHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewDefinitionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDefinitionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText wordInput;
    EditText categoryInput;
    EditText definitionInput;

    MyDBHandler dbHandler = new MyDBHandler(MainActivity.getMainActivity().getApplicationContext(),null,null,1);
    Button createBtn;
    Button importBtn;



    public NewDefinitionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewDefinitionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDefinitionFragment newInstance(String param1, String param2) {
        NewDefinitionFragment fragment = new NewDefinitionFragment();
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
        View v = inflater.inflate(R.layout.fragment_new_definition, container, false);
        createBtn = (Button)v.findViewById(R.id.createBtn);
        importBtn = (Button)v.findViewById(R.id.ImportBtn);
        wordInput = (EditText)v.findViewById(R.id.wordInput);
        categoryInput = (EditText)v.findViewById(R.id.categoryInput);
        definitionInput = (EditText)v.findViewById(R.id.definitionInput);

        //setting the onclickListeners

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validation must be done before variables are saved
                if(!validateWord(wordInput.getText().toString())){
                    wordInput.setError("Invalid Word Title");
                    wordInput.requestFocus();
                }else if (!validateCategory(categoryInput.getText().toString())){
                    categoryInput.setError("Invalid Category Name");
                    categoryInput.requestFocus();
                }else if (!validateDefinition(definitionInput.getText().toString())){
                    definitionInput.setError("Invalid Definition");
                    definitionInput.requestFocus();
                }else {
                    // validation succesfully passed
                    // proceed...
                    //save category as a variable because it is used twice
                    String _category = categoryInput.getText().toString();
                    //function here to make a new word
                    Word word = new Word(wordInput.getText().toString(),_category,definitionInput.getText().toString());
                    dbHandler.addWord(word);
                    //function here to make a new category
                    dbHandler.addCategory();
                    Toast.makeText(getContext(),"new word added",Toast.LENGTH_LONG).show();
                    MainActivity.getMainActivity().loadCategoriesFragment();
                }
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().loadImportFragment();

                //old redundant code for deleting a word

                //function here to  delete a word
                //String _category = categoryInput.getText().toString();
                //String _wordTitle = wordInput.getText().toString();
                //dbHandler.deleteWord(_wordTitle,_category);
                /*
                an extra function must be called just before
                the word gets deleted to query the database and find
                out if the any other words have the same category
                if not then the category is also deleted.
                */
            }
        });
        return v;
    }

    /*returns true if user inputed a name for a word that is greater
    than 3 characters long
    */
    private boolean validateWord(String word) {
        if (word!= null && word.length()>=3 && word.length() <= 50){
            return true;
        }else{
            return false;
        }
    }

    /*returns true if user inputed a category name that is greater
    than 2 characters long
    */
    private boolean validateCategory(String category){
        if (category!= null && category.length()>=2 && category.length() <= 50){
            return true;
        }else {
            return false;
        }
    }

    /*returns true if user inputed a name for a word that is greater
    than 3 characters long, preferably a definition should be longer
    but I do not want to unnecessarily restrict my users
    */
    private boolean validateDefinition(String definition){
        if (definition!= null && definition.length()>=3 && definition.length() <= 500){
            return true;
        }else{
            return false;
        }
    }


}
