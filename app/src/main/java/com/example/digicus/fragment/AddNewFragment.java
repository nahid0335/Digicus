package com.example.digicus.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digicus.DialogSuccess;
import com.example.digicus.R;
import com.example.digicus.database.DbHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewFragment extends Fragment {
    TextInputLayout textInputLayoutTitle;
    Button buttonSave;

    DbHelper dbHelper;

    public AddNewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new, container, false);

        textInputLayoutTitle = view.findViewById(R.id.textInputLayout_AddNewFragment_title);
        buttonSave = view.findViewById(R.id.button_addNewFragment_save);

        dbHelper = new DbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();


        buttonSave.setOnClickListener(v -> {
            String title = textInputLayoutTitle.getEditText().getText().toString().trim();
            if(title.isEmpty()){
                textInputLayoutTitle.setError("Can't be empty !!");
            }else{
                textInputLayoutTitle.setError(null);

                Boolean exists = dbHelper.CheckTitle(title);
                if(exists){
                    textInputLayoutTitle.setError("Title exists !! Please enter unique title .");
                }
                else{
                    textInputLayoutTitle.setError(null);
                    long rowid = dbHelper.InsertFinance(title);
                    if(rowid>0){
                        DialogSuccess dialogSuccess = new DialogSuccess();
                        dialogSuccess.show(getFragmentManager(),"success");
                    }
                }
            }
        });

        return view;
    }
}