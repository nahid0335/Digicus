package com.example.digicus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digicus.database.DbHelper;
import com.example.digicus.model.Details;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SingleDetailsActivity extends AppCompatActivity {
    RadioButton radiobuttonCash;
    DbHelper dbHelper;
    long financeid;
    double op1=0.0 ,op2=0.0 , result;
    private static final String TAG = "MyActivity";

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent(SingleDetailsActivity.this,DetailsActivity.class);
        returnIntent.putExtra("financeid",financeid);
        startActivity(returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_details);

        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        financeid = intent.getExtras().getLong("Id");

        TextView textViewTitle = findViewById(R.id.textView_singleDetails_title);
        TextView textViewOpcode = findViewById(R.id.textView_singleDetails_opcode);

        TextInputLayout textInputLayoutDetails = findViewById(R.id.textInputLayout_singleDetails_details);
        TextInputLayout textInputLayoutoperand1 = findViewById(R.id.textInputLayout_singleDetails_operand1);
        TextInputLayout textInputLayoutoperand2 = findViewById(R.id.textInputLayout_singleDetails_operand2);
        TextInputLayout textInputLayoutbalance = findViewById(R.id.textInputLayout_singleDetails_balance);

        Button buttomSave = findViewById(R.id.button_singleDetails_save);

        RadioGroup radioGroupcash = findViewById(R.id.radioGroup_singleDetails);
        RadioGroup radioGroupopcode = findViewById(R.id.radioGroup_singleDetails_opcode);

        textViewTitle.setText(title);

        dbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        radioGroupopcode.setOnCheckedChangeListener((group, checkedId) -> {
            String operand1 = textInputLayoutoperand1.getEditText().getText().toString().trim();
            String operand2 = textInputLayoutoperand2.getEditText().getText().toString().trim();
            switch (checkedId) {
                case R.id.radioButton_singleDetails_add:
                    textViewOpcode.setText("+");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1+op2;
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_singleDetails_sub:
                    textViewOpcode.setText("-");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1-op2;
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_singleDetails_mul:
                    textViewOpcode.setText("*");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1*op2;
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_singleDetails_div:
                    textViewOpcode.setText("/");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        if(op2 !=0.0){
                            double result = op1/op2;
                            textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                        }
                    }
                    break;
                case R.id.radioButton_singleDetails_percentage:
                    textViewOpcode.setText("%");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1*((op2/100)+1);
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
            }
        });

        buttomSave.setOnClickListener(v -> {
            String details = textInputLayoutDetails.getEditText().getText().toString().trim();
            if(details.isEmpty()){
                textInputLayoutDetails.setError("Can't be empty !!");
            }else{
                int selectedId = radioGroupcash.getCheckedRadioButtonId();
                if(selectedId == -1){
                    Toast.makeText(this,"Please select transaction type !",Toast.LENGTH_LONG).show();
                }else{
                    radiobuttonCash = findViewById(selectedId);
                    String balancetype = radiobuttonCash.getText().toString();

                    String operand1 = textInputLayoutoperand1.getEditText().getText().toString().trim();
                    String operand2 = textInputLayoutoperand2.getEditText().getText().toString().trim();
                    String operand = textViewOpcode.getText().toString();
                    if(!operand1.isEmpty() && !operand2.isEmpty()){
                        int selectedIdOperand = radioGroupopcode.getCheckedRadioButtonId();
                        if(selectedIdOperand == -1){
                            Toast.makeText(this,"Please select Mathematical operator !!",Toast.LENGTH_LONG).show();
                        }else{
                            op1 = Double.parseDouble(operand1);
                            op2 = Double.parseDouble(operand2);
                            switch (operand){
                                case "+" :
                                        result = op1+op2;
                                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                                break;
                                case "-":
                                        result = op1-op2;
                                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                                    break;
                                case "*":
                                        result = op1*op2;
                                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                                    break;
                                case "/":
                                        if(op2 !=0.0){
                                            result = op1/op2;
                                            textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                                        }else{
                                            textInputLayoutoperand2.setError("Math Error !!!");
                                        }
                                    break;
                                case "%":
                                        result = op1*((op2/100)+1);
                                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                                    break;
                            }

                        }
                    }

                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        op1 = Double.parseDouble(operand1);
                        op2 = Double.parseDouble(operand2);
                    }

                    if(textInputLayoutbalance.getEditText().getText().toString().trim().isEmpty()){
                        textInputLayoutbalance.setError("Can't be empty !!");
                    }else{
                        double balance = Double.parseDouble(textInputLayoutbalance.getEditText().getText().toString().trim());

                        String createdAt = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));
                        //Log.v(TAG,operand);
                        long rowid = dbHelper.InsertDetails(details,balancetype,op1,op2,operand,balance,createdAt,financeid);
                        if(rowid>0){
                            double currentBalance = dbHelper.FetchBalanceByFinanceId(financeid);
                            if(balancetype.equals("Cash In")){
                                currentBalance+=balance;
                            }else{
                                currentBalance-=balance;
                            }
                            boolean updateBalance = dbHelper.UpdateFinanceBalanceById(financeid,currentBalance);
                            if(!updateBalance){
                                DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                            }else{
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                                alertDialogBuilder.setMessage("Data is saved Successfully !!!")
                                                  .setTitle("Alert !")
                                                  .setPositiveButton("Done",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        Intent newintent = new Intent(SingleDetailsActivity.this,DetailsActivity.class);
                                                        newintent.putExtra("financeid",financeid);
                                                        startActivity(newintent);
                                                        finish();
                                                    }
                                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
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