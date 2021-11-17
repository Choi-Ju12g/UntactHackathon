package com.example.howmanyseats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ListView listView = findViewById(R.id.lv_userComment);

        MyAdapter adapter = new MyAdapter();
        adapter.addItem(new userComment("최정우","1999.06.19","I hate this program",3));
        adapter.addItem(new userComment("이기태","1999.02.23","I want wide room",1));
        adapter.addItem(new userComment("이상윤","1999.05.21","fuck DB",5));
        adapter.addItem(new userComment("고정훈","2000.02.23","why this is wrong",2));
        listView.setAdapter(adapter);

    }

    class MyAdapter extends BaseAdapter{
        private ArrayList<userComment> comments = new ArrayList<>();

        public void addItem(userComment comment){
            comments.add(comment);
        }
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}