package com.example.digicus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.digicus.R;
import com.example.digicus.model.Account;
import com.example.digicus.model.Finance;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddExistingAdapter extends RecyclerView.Adapter<AddExistingAdapter.ViewHolder> {
    ArrayList<Account> accounts;
    Context context;

    public AddExistingAdapter(ArrayList<Account> accounts, Context context) {
        this.accounts = accounts;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_addexisting_recyclerview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddExistingAdapter.ViewHolder holder, int position) {
        final Account account = accounts.get(position);
        holder.textTitle.setText(account.getAccountName());
        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(account.isIschecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                account.setIschecked(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public String getTitleifCheck(int position){
        if(accounts.get(position).isIschecked()){
            return accounts.get(position).getAccountName();
        }else{
            return "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textView_addexistingRecyclerview_title);
            checkBox = itemView.findViewById(R.id.checkBox_addExisting_recyclerview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!checkBox.isChecked()){
                        checkBox.setChecked(true);
                    }else{
                        checkBox.setChecked(false);
                    }
                }
            });

        }
    }
}
