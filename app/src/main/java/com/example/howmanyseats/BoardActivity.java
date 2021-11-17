package com.example.howmanyseats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    ArrayList<userComment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        this.initData();

        ListView listView = (ListView) findViewById(R.id.uc_lv_userComment);
        MyAdapter adapter = new MyAdapter(this,commentDataList);
        listView.setAdapter(adapter);
    }

    class MyAdapter extends BaseAdapter{
        ArrayList<userComment> comments;
        Context mContext = null;
        LayoutInflater mLayoutInflater = null;

        public MyAdapter(Context c,ArrayList<userComment> data){
            mContext = c;
            comments = data;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        public void addItem(userComment comment){
            comments.add(comment);
        }
        @Override
        public int getCount() {
            return comments.size();
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mLayoutInflater.inflate(R.layout.user_comment,null);

            ImageButton profile;
            TextView username,date,comment;
            profile = (ImageButton)view.findViewById(R.id.uc_profile_btn);
            username = (TextView) view.findViewById(R.id.uc_username);
            date = (TextView) view.findViewById(R.id.uc_date);
            comment = (TextView) view.findViewById(R.id.uc_comment);

            //profile.setImageDrawable(comments.get(position));
            username.setText(comments.get(position).getUser_name());
            date.setText(comments.get(position).getDate());
            comment.setText(comments.get(position).getComment());

            return view;
        }
    }

    public void initData(){
        commentDataList = new ArrayList<userComment>();
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
        commentDataList.add(new userComment("최정우","1999.06.19","I hate this program",3));
        commentDataList.add(new userComment("이기태","1999.02.23","I want wide room",1));
        commentDataList.add(new userComment("이상윤","1999.05.21","fuck DB",5));
        commentDataList.add(new userComment("고정훈","2000.02.23","why this is wrong",2));
    }
}