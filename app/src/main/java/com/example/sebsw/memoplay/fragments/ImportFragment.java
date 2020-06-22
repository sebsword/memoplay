package com.example.sebsw.memoplay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.sebsw.memoplay.R;
import com.example.sebsw.memoplay.activities.MainActivity;
import com.example.sebsw.memoplay.services.BackLoad;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    NumberPicker numberPicker;
    Button confirmImportBtn;
    String wordID = "1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ImportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImportFragment newInstance(String param1, String param2) {
        ImportFragment fragment = new ImportFragment();
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
    //new backload used to be here


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_import, container, false);
        numberPicker = (NumberPicker)v.findViewById(R.id.NumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(true);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                wordID = Integer.toString(newVal);
            }
        });


        //wordIdInput = (EditText)v.findViewById(R.id.wordIdInput);
        confirmImportBtn = (Button)v.findViewById(R.id.confirmImportBtn);
        confirmImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uses background task to  import a word at the specified ID
                BackLoad backLoad = new BackLoad();
                backLoad.execute("import",wordID);
            }
        });
        return v;
    }

}
