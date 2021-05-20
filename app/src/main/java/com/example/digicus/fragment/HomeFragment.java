package com.example.digicus.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.os.Parcelable;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.digicus.DetailsActivity;
import com.example.digicus.DialogSuccess;
import com.example.digicus.DialogUnsuccess;
import com.example.digicus.R;
import com.example.digicus.UpdateHome;
import com.example.digicus.adapter.CustomItemAnimator;
import com.example.digicus.adapter.FinanceCallBack;
import com.example.digicus.adapter.HomeAdapter;
import com.example.digicus.database.DbHelper;
import com.example.digicus.model.Finance;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements FinanceCallBack {
    DbHelper dbHelper;
    RecyclerView homeRecyclerView;
    HomeAdapter homeAdapter;
    ArrayList<Finance> finances;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeRecyclerView = view.findViewById(R.id.recyclerView_homeScreen_finance);
        homeRecyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        homeRecyclerView.setLayoutManager(layoutManager);
        homeRecyclerView.setItemAnimator(new CustomItemAnimator());

        TextView textViewguideLine = view.findViewById(R.id.textView_homeFragment_guidLine);

        finances = new ArrayList<>();

        dbHelper = new DbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor results = dbHelper.FetchAllFinance();

        if(results.getCount()>0)
        {
            textViewguideLine.setVisibility(View.GONE);
            while (results.moveToNext()){
                Finance finance = new Finance();
                finance.setFinanceId(Integer.parseInt(results.getString(0)));
                finance.setFinanceTitle(results.getString(1));
                finance.setCurrentBalance(Float.parseFloat(results.getString(2)));

                finances.add(finance);

            }
            Collections.reverse(finances);

            homeAdapter = new HomeAdapter(finances,this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(homeRecyclerView);
            homeRecyclerView.setAdapter(homeAdapter);
        }else{
            textViewguideLine.setVisibility(View.VISIBLE);
        }


        return view;
    }

    @Override
    public void onFinanceItemClick(int pos, TextView title, TextView currentBalance) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        long id = dbHelper.FetchFinanceIdByTitle(title.getText().toString().trim());
        intent.putExtra("financeid",id);

        Pair<View,String> p1 = Pair.create((View)title,"titleTN");
        Pair<View,String> p2 = Pair.create((View)currentBalance,"balanceTN");

        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent,optionsCompat.toBundle());

        }
        else
            startActivity(intent);


    }

    @Override
    public void onFinanceItemLongClick(int pos, TextView title, TextView currentBalance) {
        Intent intent = new Intent(getContext(), UpdateHome.class);
        long id = dbHelper.FetchFinanceIdByTitle(title.getText().toString().trim());
        intent.putExtra("financeid",id);
        startActivity(intent);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Do you want to delete ?")
                    .setTitle("Alert !")
                    .setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    int position = viewHolder.getAdapterPosition();
                                    long id = homeAdapter.fetchFinanceIdByPosition(position);
                                    int results = dbHelper.DeleteFinanceById(id);
                                    if(results>0){
                                        DialogSuccess dialogSuccess = new DialogSuccess();
                                        dialogSuccess.show(getFragmentManager(),"success");
                                    }else{
                                        DialogUnsuccess dialogUnsuccess = new DialogUnsuccess();
                                        dialogUnsuccess.show(getFragmentManager(),"unsuccess");
                                        homeAdapter.notifyItemChanged(position);

                                    }
                                    homeAdapter.removeAt(position);
                                    homeAdapter.notifyDataSetChanged();
                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            homeAdapter.notifyDataSetChanged();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        }
    };
}