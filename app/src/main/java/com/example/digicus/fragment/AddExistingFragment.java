package com.example.digicus.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digicus.DetailsActivity;
import com.example.digicus.DialogSuccess;
import com.example.digicus.DialogUnsuccess;
import com.example.digicus.R;
import com.example.digicus.SingleDetailsActivity;
import com.example.digicus.adapter.AddExistingAdapter;
import com.example.digicus.adapter.HomeAdapter;
import com.example.digicus.database.DbHelper;
import com.example.digicus.model.Account;
import com.example.digicus.model.Finance;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddExistingFragment extends Fragment {

    DbHelper dbHelper;
    RecyclerView addexistRecyclerView;
    AddExistingAdapter addexistAdapter;
    ArrayList<Account> accounts;
    ArrayList<String> titles;
    ArrayList<Long> ids;
    boolean flag = false;

    RadioButton radiobuttonCash;
    double op1=0.0 ,op2=0.0 , result;
    private static final String TAG = "MyActivity";


    public AddExistingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_existing, container, false);

        addexistRecyclerView = view.findViewById(R.id.RecyclerView_AddExisting_accounts);
        addexistRecyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        addexistRecyclerView.setLayoutManager(layoutManager);

        TextView textViewNodata = view.findViewById(R.id.textView_AddExisting_noData);
        TextView textViewOpcode = view.findViewById(R.id.textView_AddExisting_opcode);

        TextInputLayout textInputLayoutDetails = view.findViewById(R.id.TextInputLayout_AddExisting_details);
        TextInputLayout textInputLayoutoperand1 = view.findViewById(R.id.textInputLayout_AddExisting_operand1);
        TextInputLayout textInputLayoutoperand2 = view.findViewById(R.id.textInputLayout_AddExisting_operand2);
        TextInputLayout textInputLayoutbalance = view.findViewById(R.id.textInputLayout_AddExisting_balance);

        Button buttomSave = view.findViewById(R.id.button_AddExisting_save);

        RadioGroup radioGroupcash = view.findViewById(R.id.radioGroup_AddExisting);
        RadioGroup radioGroupopcode = view.findViewById(R.id.radioGroup_AddExisting_opcode);


         accounts = new ArrayList<>();

        dbHelper = new DbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor results = dbHelper.FetchAllFinance();

        if(results.getCount()>0) {
            textViewNodata.setVisibility(View.GONE);
            while (results.moveToNext()) {
                Account account = new Account();
                account.setAccountName(results.getString(1));
                account.setIschecked(false);
                accounts.add(account);
            }
            addexistAdapter = new AddExistingAdapter(accounts,getContext());
            addexistRecyclerView.setAdapter(addexistAdapter);

        }else{
            textViewNodata.setVisibility(View.VISIBLE);
        }

        radioGroupopcode.setOnCheckedChangeListener((group, checkedId) -> {
            String operand1 = textInputLayoutoperand1.getEditText().getText().toString().trim();
            String operand2 = textInputLayoutoperand2.getEditText().getText().toString().trim();
            switch (checkedId) {
                case R.id.radioButton_AddExisting_add:
                    textViewOpcode.setText("+");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1+op2;
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_AddExisting_sub:
                    textViewOpcode.setText("-");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1-op2;
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_AddExisting_mul:
                    textViewOpcode.setText("*");
                    if (!operand1.isEmpty() && !operand2.isEmpty()){
                        double op1 = Double.parseDouble(operand1);
                        double op2 = Double.parseDouble(operand2);
                        double result = op1*op2;
                        textInputLayoutbalance.getEditText().setText(String.valueOf(result));
                    }
                    break;
                case R.id.radioButton_AddExisting_div:
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
                case R.id.radioButton_AddExisting_percentage:
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
            titles = new ArrayList<String>();
            ids = new ArrayList<Long>();
            for(int i =0 ;i<addexistAdapter.getItemCount();i++)
            {
                String temp = addexistAdapter.getTitleifCheck(i);
                if(!temp.isEmpty()){
                    titles.add(temp);
                }
            }
            for ( int j = 0;j<titles.size();j++)
            {
                ids.add(dbHelper.FetchFinanceIdByTitle(titles.get(j)));
            }
            if(titles.size()<=0){
                Toast.makeText(getContext(),"Please Select at least one Account !!",Toast.LENGTH_LONG).show();
            }else{
                String details = textInputLayoutDetails.getEditText().getText().toString().trim();
                if(details.isEmpty()){
                    textInputLayoutDetails.setError("Can't be empty !!");
                }else{
                    int selectedId = radioGroupcash.getCheckedRadioButtonId();
                    if(selectedId == -1){
                        Toast.makeText(getContext(),"Please select transaction type !",Toast.LENGTH_LONG).show();
                    }else{
                        radiobuttonCash = view.findViewById(selectedId);
                        String balancetype = radiobuttonCash.getText().toString();

                        String operand1 = textInputLayoutoperand1.getEditText().getText().toString().trim();
                        String operand2 = textInputLayoutoperand2.getEditText().getText().toString().trim();
                        String operand = textViewOpcode.getText().toString();
                        if(!operand1.isEmpty() && !operand2.isEmpty()){
                            int selectedIdOperand = radioGroupopcode.getCheckedRadioButtonId();
                            if(selectedIdOperand == -1){
                                Toast.makeText(getContext(),"Please select Mathematical operator !!",Toast.LENGTH_LONG).show();
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
                            for(int i=0;i<ids.size();i++){
                                long rowid = dbHelper.InsertDetails(details,balancetype,op1,op2,operand,balance,createdAt,ids.get(i));
                                Log.v(TAG,ids.get(i)+"");
                                if(rowid>0){
                                    double currentBalance = dbHelper.FetchBalanceByFinanceId(ids.get(i));
                                    if(balancetype.equals("Cash In")){
                                        currentBalance+=balance;
                                    }else{
                                        currentBalance-=balance;
                                    }
                                    boolean updateBalance = dbHelper.UpdateFinanceBalanceById(ids.get(i),currentBalance);
                                    if(!updateBalance){
                                        Toast.makeText(getContext(),"Balance update is failed !!",Toast.LENGTH_LONG).show();
                                    }else{
                                        flag = true;
                                    }
                                }else{
                                    Toast.makeText(getContext(),"Balance insert is failed !!",Toast.LENGTH_LONG).show();
                                }
                            }
                            if(flag){
                                flag= false;
                                DialogSuccess dialogSuccess = new DialogSuccess();
                                dialogSuccess.show(getFragmentManager(),"success");
                            }
                            else{
                                DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                dialogUnsuccess.show(getFragmentManager(),"unsuccess");
                            }
                        }
                    }
                }
            }
        });

        return view;
    }
}