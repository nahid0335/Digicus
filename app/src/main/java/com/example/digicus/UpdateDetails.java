package com.example.digicus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digicus.database.DbHelper;
import com.example.digicus.model.Details;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateDetails extends AppCompatActivity {

    DbHelper dbHelper;
    String Cdetails,Ctype,Copcode;
    long financeId;
    double op1=0.0 ,op2=0.0 , result;
    double Cop1,Cop2,CBalance;
    private static final String TAG = "MyActivity";


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent(UpdateDetails.this,DetailsActivity.class);
        returnIntent.putExtra("financeid",financeId);
        startActivity(returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        long detailsId = getIntent().getExtras().getLong("detailsid");
        financeId = getIntent().getExtras().getLong("financeid");

        TextInputLayout textInputLayoutDetails = findViewById(R.id.textInputLayout_updateDetails_details);
        TextInputLayout textInputLayoutOperand1 = findViewById(R.id.textInputLayout_updateDetails_operand1);
        TextInputLayout textInputLayoutOperand2 = findViewById(R.id.textInputLayout_updateDetails_operand2);
        TextInputLayout textInputLayoutBalance = findViewById(R.id.textInputLayout_updateDetails_balance);

        TextView textViewOpcode = findViewById(R.id.textView_updateDetails_opcode);

        Button buttonSaveU = findViewById(R.id.button_updateDetails_save);

        RadioGroup radioGroupcash = findViewById(R.id.radioGroup_updateDetails);
        RadioGroup radioGroupopcode = findViewById(R.id.radioGroup_updateDetails_opcode);

        dbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = dbHelper.FetchDetailByDetailId(detailsId);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                Cdetails = cursor.getString(1);
                Ctype = cursor.getString(2);
                Cop1 = Double.parseDouble(cursor.getString(3));
                Cop2 = Double.parseDouble(cursor.getString(4));
                Copcode = cursor.getString(5);
                CBalance = Double.parseDouble(cursor.getString(6));

            }
        }

        cursor.close();

        textInputLayoutDetails.getEditText().setText(Cdetails);
        textInputLayoutOperand1.getEditText().setText(String.valueOf(Cop1));
        textInputLayoutOperand2.getEditText().setText(String.valueOf(Cop2));
        textInputLayoutBalance.getEditText().setText(String.valueOf(CBalance));

        textViewOpcode.setText(Copcode);
        switch (Copcode){
            case "+" :
                radioGroupopcode.check(R.id.radioButton_updateDetails_add);
                break;
            case "-":
                radioGroupopcode.check(R.id.radioButton_updateDetails_sub);
                break;
            case "*":
                radioGroupopcode.check(R.id.radioButton_updateDetails_mul);
                break;
            case "/":
                radioGroupopcode.check(R.id.radioButton_updateDetails_div);
                break;
            case "%":
                radioGroupopcode.check(R.id.radioButton_updateDetails_percentage);
                break;
        }

        if(Ctype.equals("Cash In")){
            radioGroupcash.check(R.id.radioButton_updateDetails_cashIn);
        }else{
            radioGroupcash.check(R.id.radioButton_updateDetails_cashOut);
        }


        radioGroupopcode.setOnCheckedChangeListener((group, checkedId) -> {
            String operand1 = textInputLayoutOperand1.getEditText().getText().toString().trim();
            String operand2 = textInputLayoutOperand2.getEditText().getText().toString().trim();
            switch (checkedId) {
                case R.id.radioButton_updateDetails_add:
                    textViewOpcode.setText("+");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1+op2;
                        textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_updateDetails_sub:
                    textViewOpcode.setText("-");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1-op2;
                        textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_updateDetails_mul:
                    textViewOpcode.setText("*");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1*op2;
                        textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_updateDetails_div:
                    textViewOpcode.setText("/");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        if(op2 !=0.0){
                            double result = op1/op2;
                            textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                        }
                    }
                    break;
                case R.id.radioButton_updateDetails_percentage:
                    textViewOpcode.setText("%");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1*((op2/100)+1);
                        textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
            }
        });

        buttonSaveU.setOnClickListener(v -> {
            String detailsTitle = textInputLayoutDetails.getEditText().getText().toString().trim();
            if(detailsTitle.isEmpty()){
                textInputLayoutDetails.setError("Can't be empty !!");
            }else{
                int selectedId = radioGroupcash.getCheckedRadioButtonId();
                if(selectedId == -1){
                    Toast.makeText(this,"Please select transaction type !",Toast.LENGTH_LONG).show();
                }else{
                    RadioButton radiobuttonCash = findViewById(selectedId);
                    String balancetype = radiobuttonCash.getText().toString().trim();

                    String operand1 = textInputLayoutOperand1.getEditText().getText().toString().trim();
                    String operand2 = textInputLayoutOperand2.getEditText().getText().toString().trim();
                    String operand = textViewOpcode.getText().toString().trim();
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
                                    textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                                    break;
                                case "-":
                                    result = op1-op2;
                                    textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                                    break;
                                case "*":
                                    result = op1*op2;
                                    textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                                    break;
                                case "/":
                                    if(op2 !=0.0){
                                        result = op1/op2;
                                        textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                                    }else{
                                        textInputLayoutOperand2.setError("Math Error !!!");
                                    }
                                    break;
                                case "%":
                                    result = op1*((op2/100)+1);
                                    textInputLayoutBalance.getEditText().setText(String.valueOf(result));
                                    break;
                            }

                        }
                    }

                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        op1 = Double.parseDouble(operand1);
                        op2 = Double.parseDouble(operand2);
                    }

                    if(textInputLayoutBalance.getEditText().getText().toString().trim().isEmpty()){
                        textInputLayoutBalance.setError("Can't be empty !!");
                    }else{
                        double balance = Double.parseDouble(textInputLayoutBalance.getEditText().getText().toString().trim());

                        String createdAt = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));
                        //Log.v(TAG,operand);
                        double previousBalance = CBalance;
                        String previoustype = Ctype;
                        if(previoustype.equals("Cash In")){
                            double curBalance = dbHelper.FetchBalanceByFinanceId(financeId);
                            curBalance -=previousBalance;
                            boolean isupdateBalance = dbHelper.UpdateFinanceBalanceById(financeId,curBalance);
                            if(!isupdateBalance){
                                DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                            }
                        }else{
                            double curBalance = dbHelper.FetchBalanceByFinanceId(financeId);
                            curBalance +=previousBalance;
                            boolean isupdateBalance = dbHelper.UpdateFinanceBalanceById(financeId,curBalance);
                            if(!isupdateBalance){
                                DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                            }
                        }

                        boolean isupdateDetails = dbHelper.UpdateDetailsById(detailsId,detailsTitle,balancetype,op1,op2,operand,balance);
                        if(isupdateDetails){
                            double currentBalance = dbHelper.FetchBalanceByFinanceId(financeId);
                            if(balancetype.equals("Cash In")){
                                currentBalance+=balance;
                            }else{
                                currentBalance-=balance;
                            }
                            boolean updateBalance = dbHelper.UpdateFinanceBalanceById(financeId,currentBalance);
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
                                                        Intent newintent = new Intent(UpdateDetails.this,DetailsActivity.class);
                                                        newintent.putExtra("financeid",financeId);
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