package com.example.howmanyseats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private List<Store> names;
    private Context context;

    public SearchAdapter(Context context, List<Store> names){
        this.context = context;
        this.names = names;
    }

    public void setList(List<String> naems){
        this.names = names;

        if(names.size() == 0){
            //names.add("검색어를 입력하세요");
        }
    }
    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview, null);
        TextView tv = v.findViewById(R.id.label);
        tv.setText(names.get(i).getStoreName());
        return v;
    }
}
