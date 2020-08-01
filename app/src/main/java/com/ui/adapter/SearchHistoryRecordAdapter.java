package com.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ui.ks.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/16.
 */

public class SearchHistoryRecordAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> historyrecord_list;

    public SearchHistoryRecordAdapter(Context context, ArrayList<String> historyrecord_list) {
        this.context = context;
        this.historyrecord_list = historyrecord_list;
    }

    @Override
    public int getCount() {
        return historyrecord_list.size();
    }

    @Override
    public Object getItem(int position) {
        return historyrecord_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Horlder horlder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_searchhistoryrecord,null);
            horlder=new Horlder();
            horlder.tv_record= (TextView) convertView.findViewById(R.id.tv_record);
            convertView.setTag(horlder);
        }else {
            horlder= (Horlder) convertView.getTag();
        }
        horlder.tv_record.setText(historyrecord_list.get(position));
        return convertView;
    }
    private class Horlder{
        TextView tv_record;
    }
}
