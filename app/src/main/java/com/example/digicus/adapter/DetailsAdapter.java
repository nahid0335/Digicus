package com.example.digicus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.digicus.R;
import com.example.digicus.model.Details;
import com.example.digicus.model.Finance;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    Context context;
    ArrayList<Details> details;
    DetailsCallBack detailsCallBack;
    private static final String TAG = "MyActivity";

    public DetailsAdapter(ArrayList<Details> details, DetailsCallBack detailsCallBack) {
        this.details = details;
        this.detailsCallBack = detailsCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_details_recyclerview, parent, false);
        return new DetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.ViewHolder holder, int position) {
        holder.textViewDate.setText(details.get(position).getDetailsCreatedAt());
        holder.textViewDetails.setText(details.get(position).getDetailsDetails());
        String type = details.get(position).getDetailstype();
        holder.textViewType.setText(type);
        Double balance = details.get(position).getDetailsBalance();
        holder.textViewBalance.setText(String.valueOf(balance));
        if(type.equals("Cash In")){
            holder.textViewType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorGreen));
            holder.textViewBalance.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorGreen));
        }else{
            holder.textViewType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorRed));
            holder.textViewBalance.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorRed));
        }

        Double operand1 = details.get(position).getDetailsOperand1();

        if(operand1>0.0){
            holder.textViewOperand1.setVisibility(View.VISIBLE);
            holder.textViewOperand2.setVisibility(View.VISIBLE);
            holder.textViewOpcode.setVisibility(View.VISIBLE);
            holder.textViewEqual.setVisibility(View.VISIBLE);
            holder.textViewOperand1.setText(String.valueOf(details.get(position).getDetailsOperand1()));
            holder.textViewOperand2.setText(String.valueOf(details.get(position).getDetailsOperand2()));
            holder.textViewOpcode.setText(details.get(position).getDetailsOpcode());
        }else{
            holder.textViewOperand1.setVisibility(View.GONE);
            holder.textViewOperand2.setVisibility(View.GONE);
            holder.textViewOpcode.setVisibility(View.GONE);
            holder.textViewEqual.setVisibility(View.GONE);
        }
        Log.v(TAG,holder.textViewOperand1.getText().toString()+" - "+operand1+" - "+details.get(position).getDetailsOperand1());

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public void removeAt(int position){
        details.remove(position);
    }

    public long getDetailsIdByPosition(int position){
        return details.get(position).getDetailsId();
    }
    public long getFinanceIdByPosition(int position){
        return details.get(position).getFinanceId();
    }

    public void removeAll(){
        int sizeofDetails = getItemCount();
        for(int i=0; i<sizeofDetails;i++){
            details.remove(i);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate,textViewDetails,textViewType,textViewBalance,
                textViewOperand1,textViewOperand2,textViewOpcode,textViewEqual;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDetails = itemView.findViewById(R.id.textView_detailsRecyclerView_details);
            textViewDate = itemView.findViewById(R.id.textView_detailsRecyclerView_date);
            textViewType = itemView.findViewById(R.id.textView_detailsRecyclerView_type);
            textViewBalance = itemView.findViewById(R.id.textView_detailsRecyclerView_balance);
            textViewOperand1 = itemView.findViewById(R.id.textView_detailsRecyclerView_operand);
            textViewOperand2= itemView.findViewById(R.id.textView_detailsRecyclerView_operand2);
            textViewOpcode = itemView.findViewById(R.id.textView_detailsRecyclerView_opcode);
            textViewEqual = itemView.findViewById(R.id.textView_detailsRecyclerView_equel);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    detailsCallBack.onDetailsItemClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }

}
