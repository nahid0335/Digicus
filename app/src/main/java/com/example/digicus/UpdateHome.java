package com.example.digicus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.digicus.database.DbHelper;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateHome extends AppCompatActivity {
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_home);

        long financeId = getIntent().getExtras().getLong("financeid");

        TextView textViewPreviousTitle = findViewById(R.id.textView_updateHome_previousTitle);
        TextInputLayout textInputLayoutNewTitle = findViewById(R.id.TextInputLayout_updateHome_title);
        Button buttonSave = findViewById(R.id.button_updateHome_save);

        dbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        String title = dbHelper.FetchFinanceTitleByFinanceID(financeId);
        textViewPreviousTitle.setText(title);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = textInputLayoutNewTitle.getEditText().getText().toString().trim();
                if(newTitle.isEmpty()){
                    textInputLayoutNewTitle.setError("Please input a title !!");
                }else{
                    boolean isexist = dbHelper.CheckTitle(newTitle);
                    if(isexist){
                        textInputLayoutNewTitle.setError("Please insert an unique Title !!");
                    }else{
                        boolean isUpdate = dbHelper.UpdateFinanceTitleById(financeId,newTitle);
                        if(isUpdate){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateHome.this);
                            alertDialogBuilder.setMessage("Data is Update Successfully !!!")
                                    .setTitle("Alert !")
                                    .setPositiveButton("Done",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    Intent newintent = new Intent(UpdateHome.this,MainActivity.class);
                                                    startActivity(newintent);
                                                    finish();
                                                }
                                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }else{
                            DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                            dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                        }
                    }
                }
            }
        });
    }
}