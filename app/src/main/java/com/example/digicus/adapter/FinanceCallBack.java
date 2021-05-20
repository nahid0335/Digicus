package com.example.digicus.adapter;

import android.widget.TextView;

public interface FinanceCallBack {
    void onFinanceItemClick(int pos,
                         TextView title,
                         TextView currentBalance);

    void onFinanceItemLongClick(int pos,
                                TextView title,
                                TextView currentBalance);
}
