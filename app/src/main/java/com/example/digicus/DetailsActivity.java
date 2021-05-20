package com.example.digicus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digicus.adapter.DetailsAdapter;
import com.example.digicus.adapter.DetailsCallBack;
import com.example.digicus.database.DbHelper;
import com.example.digicus.model.Details;
import com.example.digicus.model.Finance;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements DetailsCallBack {
    TextView textTitle,textBalance,textNoData;
    RecyclerView detailsRecycler;
    ImageView imageViewAdd,imageViewDeleteAll;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    DbHelper dbHelper;
    ArrayList<Details> details;
    DetailsAdapter detailsAdapter;
    private static final String TAG = "MyActivity";


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        dbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        textTitle = findViewById(R.id.textView_details_title);
        textBalance = findViewById(R.id.textView_details_currentBalance);
        textNoData = findViewById(R.id.textView_details_noData);

        imageViewAdd = findViewById(R.id.imageView_details_add);
        imageViewDeleteAll = findViewById(R.id.imageView_details_deleteAll);

        detailsRecycler = findViewById(R.id.recyclerView_details_data);
        detailsRecycler.setHasFixedSize(true);

        long financeid = getIntent().getExtras().getLong("financeid");

        String title = dbHelper.FetchFinanceTitleByFinanceID(financeid);
        double balance = dbHelper.FetchBalanceByFinanceId(financeid);

        textTitle.setText(title);
        textBalance.setText(String.valueOf(balance));
        if(balance>0.0){
            textBalance.setTextColor(ContextCompat.getColor(this,R.color.colorGreen));
        }else{
            textBalance.setTextColor(ContextCompat.getColor(this,R.color.colorRed));
        }



        Cursor results = dbHelper.FetchAllDetailsByFinanceId(financeid);

        details = new ArrayList<>();
        if(results.getCount()>0){
            textNoData.setVisibility(View.GONE);
            while (results.moveToNext()){
                Details detail = new Details();
                detail.setDetailsId(Integer.parseInt(results.getString(0)));
                detail.setDetailsDetails(results.getString(1));
                detail.setDetailstype(results.getString(2));
                detail.setDetailsOperand1(Double.parseDouble(results.getString(3)));
                detail.setDetailsOperand2(Double.parseDouble(results.getString(4)));
                detail.setDetailsOpcode(results.getString(5));
                detail.setDetailsBalance(Double.parseDouble(results.getString(6)));
                detail.setDetailsCreatedAt(results.getString(7));
                detail.setFinanceId(Integer.parseInt(results.getString(8)));

                details.add(detail);
            }

            detailsAdapter = new DetailsAdapter(details,this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(detailsRecycler);
            detailsRecycler.setAdapter(detailsAdapter);
            detailsRecycler.setLayoutManager(new LinearLayoutManager(this));
        }else{
            textNoData.setVisibility(View.VISIBLE);
        }

        imageViewAdd.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsActivity.this, SingleDetailsActivity.class);
            intent.putExtra("Title",textTitle.getText());
            intent.putExtra("Id",financeid);
            startActivity(intent);
            finish();
        });

        imageViewDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);
                alertDialogBuilder.setMessage("Do you want to delete all Transactions ?")
                        .setTitle("Alert !")
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        boolean results = dbHelper.DeleteAllDetailsByFinanceId(financeid);
                                        if(results){
                                            boolean isupdateBalance = dbHelper.UpdateFinanceBalanceById(financeid,0.0);
                                            if(isupdateBalance){
                                                DialogSuccess dialogSuccess = new DialogSuccess();
                                                dialogSuccess.show(getSupportFragmentManager(),"success");
                                                double newBalance = dbHelper.FetchBalanceByFinanceId(financeid);
                                                textBalance.setText(String.valueOf(newBalance));
                                                detailsAdapter.removeAll();
                                            }else{
                                                DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                                dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                                            }
                                        }else{
                                            DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                            dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                                        }
                                        detailsAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                detailsAdapter.notifyDataSetChanged();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });

    }

    @Override
    public void onDetailsItemClick(int pos) {
        long detailsId = detailsAdapter.getDetailsIdByPosition(pos);
        long financeId = detailsAdapter.getFinanceIdByPosition(pos);
        Intent intent = new Intent(DetailsActivity.this,UpdateDetails.class);
        intent.putExtra("detailsid",detailsId);
        intent.putExtra("financeid",financeId);
        startActivity(intent);
        finish();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);
            alertDialogBuilder.setMessage("Do you want to delete ?")
                    .setTitle("Alert !")
                    .setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    int position = viewHolder.getAdapterPosition();
                                    long id = detailsAdapter.getDetailsIdByPosition(position);
                                    long finId = detailsAdapter.getFinanceIdByPosition(position);
                                    int results = dbHelper.DeleteDetailsById(id);
                                    if(results>0){
                                        DialogSuccess dialogSuccess = new DialogSuccess();
                                        dialogSuccess.show(getSupportFragmentManager(),"success");
                                        detailsAdapter.notifyItemChanged(position);
                                        detailsAdapter.removeAt(position);
                                        double newBalance = dbHelper.FetchBalanceByFinanceId(finId);
                                        textBalance.setText(String.valueOf(newBalance));
                                    }else{
                                        DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                        dialogUnsuccess.show(getSupportFragmentManager(),"unsuccess");
                                        detailsAdapter.notifyItemChanged(position);

                                    }
                                    detailsAdapter.notifyDataSetChanged();
                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            detailsAdapter.notifyDataSetChanged();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        }
    };
}