package com.example.digicus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.digicus.R;
import com.example.digicus.model.Finance;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    ArrayList<Finance> finances;
    //Context context;
    FinanceCallBack financeCallBack;

    public HomeAdapter(ArrayList<Finance> finances, FinanceCallBack financeCallBack) {
        this.finances = finances;
        this.financeCallBack = financeCallBack;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        holder.textTitle.setText(finances.get(position).getFinanceTitle());
        holder.textBalance.setText(String.valueOf(finances.get(position).getCurrentBalance()));
        float balance = finances.get(position).getCurrentBalance();
        if(balance>0)
        {
            holder.textBalance.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorGreen));
            holder.icon.setImageResource(R.drawable.ic_baseline_monetization_on_24_green);
        }else{
            holder.textBalance.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed));
            holder.icon.setImageResource(R.drawable.ic_baseline_monetization_on_24_red);
        }
    }

    @Override
    public int getItemCount() {
        return finances.size();
    }

    public long fetchFinanceIdByPosition(int position){
        return finances.get(position).getFinanceId();
    }

    public void removeAt(int position){
        finances.remove(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle,textBalance;
        public ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textView_homeFragment_title);
            textBalance = itemView.findViewById(R.id.textView_homeFragment_balance);
            icon = itemView.findViewById(R.id.imageView_homeFragment_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    financeCallBack.onFinanceItemClick(getAdapterPosition(),textTitle,textBalance);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    financeCallBack.onFinanceItemLongClick(getAdapterPosition(),textTitle,textBalance);
                    return true;
                }
            });

        }
    }
}
